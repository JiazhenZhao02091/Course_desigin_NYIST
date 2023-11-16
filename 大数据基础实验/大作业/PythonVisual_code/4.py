import pymysql
from pyecharts import options as opts
from pyecharts.charts import WordCloud
from pyecharts.globals import SymbolType

#4.画出美国确诊最多的10个州—词云图

# 获取 Mysql 的链接
# conn = pymysql.connect(host='192.168.44.134'
#                        , user='root'
#                        , passwd='root'
#                        , port=3306
#                        , db='bigdata'
#                        , charset='utf8'
#                        )
conn = pymysql.connect(host='127.0.0.1'
                       , user='root'
                       , passwd='123456'
                       , port=3306
                       , db='bigdata'
                       , charset='utf8'
                       )
# 获取下标
cur = conn.cursor()
# sql = "SELECT state, SUM(cases) AS total_cases FROM us_covid WHERE date <= '2023-05-19' GROUP BY state ORDER BY total_cases DESC LIMIT 10;"
sql = "SELECT state, SUM(cases) AS total_cases FROM us_covid WHERE date = '2020-05-19' GROUP BY state ORDER BY total_cases DESC LIMIT 10;"

data = []
cur.execute(sql)
datas = cur.fetchall()
for x in datas:
    data.append(x)
print(data)


# states = []
# totalCases = []
# cur.execute(sql);
# data1 = cur.fetchall()
# for x in data1:
#     x = list(x)
#     states += x
#
# cur.execute(sql1)
# data2 = cur.fetchall()
# for x in data2:
#     x = list(x)
#     totalCases += x
#
# #转换成 [(n,v),(n,v)] 的格式
# data = [(n, v) for n, v in zip(states, totalCases)]
#

c = (
WordCloud()
.add("", data, word_size_range=[20, 100], shape=SymbolType.DIAMOND)
.set_global_opts(title_opts=opts.TitleOpts(title="美国各州确诊Top10"))
.render("F:\code\PythonView\\result4\\result.html")
)

# 关闭链接
cur.close()
conn.close()