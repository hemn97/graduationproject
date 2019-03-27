import pymysql
import numpy as np
import pandas as pd

def loadStat():
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "SELECT id,view_number,comment_number,team_number FROM v_competitions \
    where live=1"
    res = []
    try:
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            res.append([row[0], row[1], row[2], row[3]])
    except:
        print ("Error: unable to fetch data")
    db.close()
    return pd.DataFrame(res)

def calculateCurrentHeat(stats):
    weights = [1, 2, 3]
    heats = np.sum(np.multiply(stats[:,1:], weights), axis=1)
    return heats

def coolingHeat(current_heats):
    cooling_coefficient = 0.001897
    interval_hour = 1
    new_heats = np.multiply(current_heats, np.exp(-(cooling_coefficient*interval_hour)))
    return np.rint(new_heats).astype(np.int).reshape(-1,1)

def writeDB(matrix):
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "update t_competitions set heat=%s where id=%s"
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
    stats = loadStat()
    stats = np.array(stats.fillna(0))
    current_heats = calculateCurrentHeat(stats)
    new_heats = coolingHeat(current_heats)
    ids = stats[:,:1].astype(np.int).astype(np.str)
    heat_matrix = np.hstack((new_heats, ids))
    writeDB([tuple(row) for row in heat_matrix])