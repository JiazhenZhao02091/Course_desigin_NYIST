import pymysql
from pyecharts import options as opts
from pyecharts.charts import Bar
from pyecharts.charts import Line
from pyecharts.components import Table
from pyecharts.charts import WordCloud
from pyecharts.charts import Pie
from pyecharts.charts import Funnel
from pyecharts.charts import Scatter
from pyecharts.charts import PictorialBar
from pyecharts.options import ComponentTitleOpts
from pyecharts.globals import SymbolType


# 获取 Mysql 的链接
conn = pymysql.connect(host='127.0.0.1'
                       , user='root'
                       , passwd='123456'
                       , port=3306
                       , db='bigdata'
                       , charset='utf8'
                       )
# 获取下标
cur = conn.cursor()

#6.找出美国确诊最少的10个州——>词云图
data = []

sql = "SELECT state, SUM(cases) AS total_cases FROM us_covid WHERE date = '2020-05-19' GROUP BY state ORDER BY total_cases ASC LIMIT 10;"
cur.execute(sql)
datas = cur.fetchall()
for x in datas:
    data.append(x)
print(data)
print(len(data))
# sql = "select state from us_covid group by state order by sum(cases) asc limit 10; "
# sql1 = "select sum(cases) from us_covid group by state order by sum(cases) asc limit 10; "
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
.add("", data, word_size_range=[50, 20], shape=SymbolType.DIAMOND)
.set_global_opts(title_opts=opts.TitleOpts(title="美国各州确诊最少的10个州"))
.render("F:\code\PythonView\\result6\\result2.html")
)
# Northern Mariana Islands
# Vermont
# 关闭链接
cur.close()
conn.close()