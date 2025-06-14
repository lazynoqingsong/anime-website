-- 论坛帖子表
CREATE TABLE IF NOT EXISTS forum_post (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    reply_count INT DEFAULT 0 COMMENT '回复数量',
    PRIMARY KEY (id),
    INDEX idx_user (user_id),
    INDEX idx_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子';

-- 帖子回复表
CREATE TABLE IF NOT EXISTS forum_reply (
    id INT NOT NULL AUTO_INCREMENT,
    post_id INT NOT NULL COMMENT '关联的帖子ID',
    user_id INT NOT NULL COMMENT '回复用户ID',
    content TEXT NOT NULL COMMENT '回复内容',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
    PRIMARY KEY (id),
    INDEX idx_post (post_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子回复';

-- 插入一些测试数据
INSERT INTO forum_post (user_id, title, content, created_time, view_count, reply_count) VALUES
(1, '新番推荐：2024年春季必看动画', '大家好，我想分享一下今年春季新番的推荐。\n\n1. 《间谍过家家》第二季 - 继续讲述特工、杀手和超能力者组成的临时家庭的故事。\n2. 《鬼灭之刃》新章 - 继续猎鬼大业，制作精良。\n3. 《辉夜大小姐》最终季 - 天才们的恋爱喜剧即将迎来高潮。\n\n大家有什么看法？', NOW() - INTERVAL 2 DAY, 15, 2),
(2, '讨论：最喜欢的动漫角色', '想和大家聊聊你们最喜欢的动漫角色是谁，以及为什么喜欢？\n\n我最喜欢《进击的巨人》中的利威尔兵长，他的实力强大，性格冷静，而且有着复杂的过去和丰富的内心世界。虽然表面冷漠，但其实非常关心自己的同伴。\n\n你们呢？', NOW() - INTERVAL 1 DAY, 8, 1),
(1, '求推荐：类似《你的名字》的动画电影', '最近看了《你的名字》，被剧情和画面深深打动。请问有没有类似风格或者同样打动人心的动画电影推荐？谢谢大家！', NOW() - INTERVAL 4 HOUR, 5, 0);

INSERT INTO forum_reply (post_id, user_id, content, created_time) VALUES
(1, 2, '我觉得《间谍过家家》真的超级搞笑，安妮亚太可爱了！期待第二季！', NOW() - INTERVAL 1 DAY),
(1, 3, '同意楼上的看法，而且《鬼灭之刃》的画面真的太精美了，每一帧都可以当壁纸。', NOW() - INTERVAL 12 HOUR),
(2, 3, '我最爱《灌篮高手》里的三井寿，他的成长故事真的很励志，从曾经的天才到放弃篮球，再到回归球场的故事很打动人。', NOW() - INTERVAL 6 HOUR); 