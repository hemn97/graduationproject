import pymysql
import numpy as np
import pandas as pd
from collections import Counter

def loadUsers():
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "SELECT id,gender,skills,school FROM t_users WHERE role_type = 1 or role_type = 2"
    res = []
    try:
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            _id = row[0]
            gender = '0' if row[1]=='男' else '1'
            skills = row[2]
            school = row[3].split('_')[3]
            res.append([_id, gender, skills, school])
    except:
        print ("Error: unable to fetch data")
        
    db.close()
    return pd.DataFrame(res, columns=['id', 'gender', 'skills', 'school'])

def loadScores():
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "SELECT user_id,theme_id FROM t_browse_logs WHERE theme_type = '竞赛'"
    res = []
    try:
        tmp = []
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            tmp.append((row[0], row[1], 1))
        counter = Counter(tmp)
        for key in counter:
            res.append([key[0], key[1], counter[key]])
    except:
        print ("Error: unable to fetch data")
    db.close()
    return pd.DataFrame(res, columns=['user_id', 'theme_id', 'rating'])

def hammingDis(a,b):
    sumnum = 0
    for i in range(len(a)):
        if a[i]!=b[i]:
            sumnum += 1
    return sumnum

# 皮尔逊相关系数
def pearson(x, y):
    mean1, mean2 = x.mean(), y.mean() 
    # 分母 
    denominator = (sum((x-mean1)**2)*sum((y-mean2)**2))**0.5 
    try: 
        value = sum((x - mean1) * (y - mean2)) / denominator 
    except ZeroDivisionError: 
        value = 0 
    return value

# 用户属性相似度
def userAttributeSimilarity(user_attrs):
    attrs = user_attrs['gender']+user_attrs['school']
    similarity_matrix = []
    for attr in attrs:
        similarity_row = []
        for row in attrs:
            similarity_row.append(1/(hammingDis(attr,row)**11+1))
        similarity_matrix.append(similarity_row)
    return np.array(similarity_matrix)

# 用户偏好相似度
def userPreferenceSimilarity(user_preference):
    t = []
    for row in user_preference.itertuples(index=True, name='Pandas'):
        _id = getattr(row, "id")
        skills = getattr(row, "skills").split(',')
        for skill in skills:
            t.append([_id, skill, 1])
    df = pd.DataFrame(t, columns=['id', 'preference', 'rating'])
    preference_matrix = df.pivot(index='id', columns='preference', values='rating').fillna(0)
    preference_matrix = np.array(preference_matrix)
    similarity_matrix = []
    for preference in preference_matrix:
        similarity_row = []
        for row in preference_matrix:
            distance = np.dot(preference,row)/(np.linalg.norm(preference)*(np.linalg.norm(row)))
            similarity_row.append(distance)
        similarity_matrix.append(similarity_row)
    return np.array(similarity_matrix)

# 用户联合相似度
def userUnionSimilarity(attr_similarity, preference_similarity):
    k = 0.5
    return np.add(k*attr_similarity, (1-k)*preference_similarity)

def scoreMatrix(data):
    return data.pivot(index='theme_id', columns='user_id', values='rating').fillna(0)

# 寻找用户最近邻并生成推荐结果
def getRecommendations(N, user_similarity, score_matrix):
    res = []
    for user_index, user_row in user_similarity.iterrows():   # 循环遍历每个用户
        tmp = []
        if user_index in score_matrix.columns:
            mean_score_a = score_matrix[user_index].mean()
        else:
            mean_score_a = 0
        for item_index, item_row in score_matrix.iterrows():   # 循环遍历项目p
            part1 = 0
            for sim_user_index in user_row.index: # 循环遍历最近邻用户
                if user_index == sim_user_index:
                    continue
                if sim_user_index in score_matrix.columns:
                    mean_score_b = score_matrix[sim_user_index].mean()
                else:
                    mean_score_b = 0
                sim = user_row[sim_user_index]
                if sim_user_index in score_matrix.columns:
                    score_b = item_row[sim_user_index]
                else:
                    score_b = 0
                part1 += sim*(score_b-mean_score_b)
            pred = mean_score_a + part1/user_row.sum()
            tmp.append([user_index, item_index, pred])
        _sorted = sorted(tmp, key=lambda x:x[2], reverse = False)[1:N+1]
        res += _sorted
    return pd.DataFrame(res, columns=['user_id', 'theme_id', 'pred']).astype('str')

def handleRecommendations(recommendations):
    res = []
    for index, row in recommendations.iterrows():
            res.append((row['user_id'], row['theme_id'], row['pred']))
    return res  

def clearDB():
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "delete from t_recommendations"
    try:
        cursor.execute(sql)
        db.commit()
    except:
        # 发生错误时回滚
        db.rollback()
        print ("Error: unable to clear data")
    db.close()

def writeDB(data):
    db = pymysql.connect("localhost", "root", "", "competitionplatform" )
    cursor = db.cursor()
    sql = "INSERT INTO t_recommendations(theme_type, \
       user_id, theme_id, pred, update_time) \
       VALUES ('竞赛', %s, %s, %s, now())"
    try:
        cursor.executemany(sql, data)
        db.commit()
    except Exception as e:
        # 发生错误时回滚
        db.rollback()
        print ("Error: unable to write data")
    db.close()

if __name__ == '__main__': 
    users = loadUsers()
    user_attrs = users[['id', 'gender', 'school']]
    attr_similarity = userAttributeSimilarity(user_attrs)
    user_preference = users[['id', 'skills']]
    preference_similarity = userPreferenceSimilarity(user_preference)
    _id = np.array(users['id'])
    user_similarity = pd.DataFrame(userUnionSimilarity(attr_similarity, preference_similarity), \
                      columns=_id, index=_id)
    scores = loadScores()
    recommendations = getRecommendations(3, user_similarity, scoreMatrix(scores))
    clearDB()
    writeDB(handleRecommendations(recommendations))
    