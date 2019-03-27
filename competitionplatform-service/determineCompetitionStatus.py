import pymysql
import numpy as np
import time

def loadInfo():
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "SELECT id,start_time,end_time FROM t_competitions where live=1"
    res = []
    try:
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            res.append([row[0], row[1], row[2]])
    except:
        print ("Error: unable to fetch data")
    db.close()
    return np.array(res).astype(np.str)

def handleStatus(info):
    start_time = info[:,1:2]
    end_time = info[:,2:3]
    now = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
    status = []
    for index in range(len(info)):
        if (start_time[index] <= now and now < end_time[index]):
            status.append('进行中')
        elif (now < start_time[index]):
            status.append('可报名')
        elif (now >= end_time[index]):
            status.append('已结束')
    status = np.array(status).reshape(-1,1)
    ids = info[:,:1].astype(np.str).reshape(-1,1)
    status_matrix = np.hstack((status, ids))
    return status_matrix

def writeDB(matrix):
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "update t_competitions set status=%s where id=%s"
    try:
        cursor.executemany(sql, matrix)
        db.commit()
    except Exception as e:
        # 发生错误时回滚
        db.rollback()
        print ("Error: unable to write data")
        print(e)
    db.close()
        
if __name__ == '__main__': 
    info = loadInfo()
    status_matrix = handleStatus(info)
    writeDB([tuple(row) for row in status_matrix])
    
    