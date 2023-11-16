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
state = []
totalCases = []
totalDeaths = []
deathRate = []
sql = "SELECT state, SUM(cases) AS total_cases, SUM(deaths) AS total_deaths, (SUM(deaths) / SUM(cases)) * 100 AS case_fatality_rate FROM us_covid WHERE date = '2020-05-19' GROUP BY state ORDER BY total_cases DESC;"
cur.execute(sql)
data = cur.fetchall()
for a,b,c,d in data:
    state.append(a)
    totalCases.append(b)
    totalDeaths.append(c)
    deathRate.append(d)

allState = []
allState = [i for i in zip(state,totalCases,totalDeaths,deathRate)]
table = Table()

headers = ["State name", "Total cases", "Total deaths", "Death rate"]
rows = allState
table.add(headers, rows)
table.set_global_opts(
    title_opts=ComponentTitleOpts(title="美国各州疫情一览", subtitle="")
)
table.render("F:\code\PythonView\\result3\\result.html")



# 关闭链接
cur.close()
conn.close()