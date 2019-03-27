import pymysql
import numpy as np

def calMatrix():
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "SELECT video_id,count(*),avg(score) FROM t_video_evaluations \
    where live=1 group by video_id"
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
    return np.array(res, dtype=np.dtype(float))

def writeDB(ranks):
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "update t_videos set score=%s where id=%s"
    try:
        cursor.executemany(sql, ranks)
        db.commit()
    except Exception as e:
        # 发生错误时回滚
        db.rollback()
        print ("Error: unable to write data")
        print(e)
        
    db.close()


def imdb_rank(matrix):
    minimum_votes = 10  # 投票数的最低阈值
    correctly_votes_rate = np.average(matrix[:,2])  # 所有视频的平均得分
    votes_number = matrix[:,1]  # 该视频的投票数
    average_rating = matrix[:,2]    # 该视频的平均得分
    ids = matrix[:,0].astype(np.int).astype(np.str).reshape(-1,1)
    scores = np.add(np.multiply(np.divide(votes_number, (votes_number+minimum_votes)), \
                               average_rating), np.multiply(np.divide(minimum_votes, \
                                             (votes_number+minimum_votes)), correctly_votes_rate))
    scores = np.around(scores, decimals=1).reshape(-1,1)
    score_matrix = np.hstack((scores, ids))
    return score_matrix
    
if __name__ == '__main__': 
    matrix = calMatrix()  # 得到评分矩阵
    ranks = imdb_rank(matrix)
    writeDB([tuple(row) for row in ranks])