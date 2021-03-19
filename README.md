# 安卓课程git仓库
谢乐凡 518111910022
- 2021-03-07  
此前提到过分支管理混乱，因此删除了原本的仓库重新创建了这个仓库（同名的，不知道能不能用原来的链接访问）
- 环境提示  
android studio中的`gradle`版本为6.5.0，而不是文档中提到的5.1.0（是这个吧），当时看android studio下下来后里面默认的是前一个版本所以下载的时候就选择该版本了  
调试时使用的是自己的手机，android版本为10.0  
- 2021-03-05 第一次作业  
列表与点击跳转已实现，搜索已实现
每一次search实际是在前一次search结果的基础上进行搜索，因此增加了重置按钮刷新页面到初始数据状态



## week two

- 2021-03-11

  上传作业文件夹（`second_class`）同时更新`README.md`

  将所有作业放在练习三的pager管理中，每一项对应一个页面

  - exercise 1

    进度条调节动画进度至指定状态，到终点则设置为自动播放，同时也可以直接点击勾选进入自动播放或取消勾选并恢复进度条至开始

  - exercise 2

    关于颜色选择没找到好的方式，所以直接copy了老师您项目中调用的git库，速度设置增加了一个按钮来进行确定，因为`EditText`控件的几个监听类没有找到合适的使用方法

    写该作业时没有搜索到`fragment`控件中`Context`的获取方式（而`ColorPicker`中又需要），因此非常蠢地另写了一个按钮启动新的`Activity`（做练习三时搜到了`getActivity`，但懒得改了）

  - exercise 3

    载入界面与用户列表的消失与出现各占`2.5s`

    没有取消预加载，因此在`page2`停留后再前往`page3`是会无动画效果的



# week three

- 2021-03-19

  上传作业文件夹（`third_class`）同时更新`README.md`

  - 刻度值只写了0、3、6、9四个时刻，将刻度线往中心缩了一点后空出空间写刻度值，文字绘制的对齐非常笨拙地通过尝试试出来的

    文字设置对齐时标准是文字底部因此刻度值书写的位置，有点麻烦

  - 通过在`ondraw`中通过`handler`延迟`1s`再次调用`ondraw`同时模拟时间过去一秒来实现指针跳动

    将时间间隔缩短100倍（即`0.01s`）使秒针跳动10000次，没有遇到问题

  - 尝试增加了两个函数获取时间与设置时钟的当前时间，但调用后都发现时钟转动加快且不稳定，一段时间之后恢复正常

    时间规划有问题没有时间继续调试了，将该内容注释起来了