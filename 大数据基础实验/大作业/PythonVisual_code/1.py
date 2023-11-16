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
cases = []
deaths = []
date = []
sql = "SELECT date, SUM(cases) AS total_cases, SUM(deaths) AS total_deaths FROM us_covid GROUP BY date ORDER BY date;"
cur.execute(sql)
data = cur.fetchall()
for a,b,c in data:
    date.append(a)
    cases.append(b)
    deaths.append(c)

d = (
    Bar()
    .add_xaxis(date)
    .add_yaxis("累计确诊人数", cases, stack="stack1")
    .add_yaxis("累计死亡人数", deaths, stack="stack1")
    .set_series_opts(label_opts=opts.LabelOpts(is_show=False))
    .set_global_opts(title_opts=opts.TitleOpts(title="美国每日累计确诊和死亡人数"))
    .render("F:\code\PythonView\\result1\\result.html")
)


# 关闭链接
cur.close()
conn.close()