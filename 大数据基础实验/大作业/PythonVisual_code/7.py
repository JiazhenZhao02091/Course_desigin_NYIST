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
                       , passwd='root'
                       , port=3306
                       , db='bigdata'
                       , charset='utf8'
                       )
# 获取下标
cur = conn.cursor()
data =[]
# state = []
# totalDeaths = []


# 找出美国死亡最少的10个州——>漏斗图

sql = "SELECT state, SUM(deaths) AS total_deaths FROM us_covid WHERE date = '2020-05-19'   GROUP BY state  ORDER BY total_deaths ASC   LIMIT 10;"

cur.execute(sql)
datas = cur.fetchall()
for x in datas:
    data.append(x)
# sql = "select state from us_covid group by state order by sum(deaths) asc limit 10; "
# cur.execute(sql)
# datas = cur.fetchall()
# for x in datas:
#     x = list(x)
#     state += x
# sql = "select sum(deaths) from us_covid group by state order by sum(deaths) asc limit 10; "
# cur.execute(sql)
# data = cur.fetchall()
# for x in data:
#     x = list(x)
#     totalDeaths += x
#
#
# #转换成 [(n,v),(n,v)] 的格式
# data = [(n, v) for n, v in zip(state, totalDeaths)]

c = (
Funnel()
.add(
    "State",
    data,
    sort_="ascending",
    label_opts=opts.LabelOpts(position="inside"),
)
.set_global_opts(title_opts=opts.TitleOpts(title=""))
.render("F:\code\PythonView\\result7\\result.html")
)

# 关闭链接
cur.close()
conn.close()