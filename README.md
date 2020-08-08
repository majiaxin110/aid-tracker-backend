# AidTracker

A backend project that provides api service for aid tracker wechat mini program and web page.
Helping people track their donations and make sure that people who really need the resources get them.

设计实现总结（2020.8）
本项目是我首次尝试将领域驱动设计的一些思想应用到实际。目前来看，领域层确实做到了一定的抽象和分离。
一些Entity和Domain Primitive的职责做到了较为清晰的划分。
考虑到需求整体较为简单，领域层目前与底层数据库耦合较为严重，整体可测性不是很高。此外，可考虑继续在如下方向迭代

- 修改、抽象部分web.service下部分业务逻辑至领域层形成领域服务。注意领域服务不能有过多依赖
- 继续参考 [阿里的相关文章](https://developer.aliyun.com/article/719251) 抽象形成Infrastructure模块
- 通过设置Builder，提高领域层的封装性，避免到处@Data
- 拼字段这种东西不管到哪里都很恶心，还是得考虑搞一层拼字段