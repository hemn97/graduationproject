import pandas as pd

def loadData():
    filePath = './school.csv'
    df = pd.read_csv(filePath, encoding = 'gb18030').astype('str')
    return df

def group(data):
    group_by_province = data.groupby('province')
    provinceOptions = []
    for province,province_group in group_by_province:
        print(province)
        group_by_city = province_group.groupby('city')
        cityOptions = []
        for city,city_group in group_by_city:
            df = pd.DataFrame()
            df['value'] = city_group['province']+'_'+city_group['city']+'_'+ \
                city_group['name']+'_'+city_group['code']
            df['label'] = city_group['name']
            children = df.to_json(orient='records', force_ascii=False)
            cityOptions.append('{"value":"%s", "label":"%s", "children":%s}' % (city, city, children))
        provinceOptions.append('{"value":"%s", "label":"%s", "children":[%s]}' % \
            (province, province, ",".join(cityOptions)))
    return "[%s]" % ",".join(provinceOptions)

def saveData(data):
    fh = open('schoolOptions.txt', 'w+', encoding='utf-8')
    fh.write(data)
    fh.close()

if __name__ == '__main__': 
    data = loadData()
    json = group(data)
    saveData(json)