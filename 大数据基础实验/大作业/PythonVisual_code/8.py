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

# 美国的病死率--->饼状图

sql = "SELECT (SUM(deaths) / SUM(cases)) * 100 AS case_fatality_rate    FROM us_covid   WHERE date = '2020-05-19';"
cur.execute(sql)
data = cur.fetchone()
# print(data[0])
death_value = float(data[0])
print(death_value)

values = [("Death(%)", death_value),("No-Death(%)",100-death_value)]

c = (
Pie()
.add("", values)
.set_colors(["blcak","orange"])
.set_global_opts(title_opts=opts.TitleOpts(title="全美的病死率"))
.set_series_opts(label_opts=opts.LabelOpts(formatter="{b}: {c}"))
.render("F:\code\PythonView\\result8\\result.html")
)


# 关闭链接
cur.close()
conn.close()