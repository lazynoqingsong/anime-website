/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : anime

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 06/03/2025 13:13:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for anime
-- ----------------------------
DROP TABLE IF EXISTS `anime`;
CREATE TABLE `anime`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '动漫标题',
  `cover_image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '封面图片URL',
  `release_date` date NULL DEFAULT NULL COMMENT '首播日期',
  `status` enum('连载','完结','未播放') CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '播放状态',
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '剧情简介',
  `studio` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '制作公司',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `broadcast_day` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '播放星期(周一~周日)',
  `broadcast_time` time NULL DEFAULT NULL COMMENT '播放时间',
  `format` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `genres` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of anime
-- ----------------------------
INSERT INTO `anime` VALUES (1, '青之驱魔师  终夜篇', 'anime01.jpg', '2024-01-06', '连载', '雪男向燐告别，前往光明会。为了面对寻求力量、寻求真理的雪男，燐决定有必要知道他们出生的秘密，在梅菲斯特的指引下前往过去。燐是养父藤本狮郎和生母百合·追寻着艾金的足迹，两人走过的道路，比想象中更加残酷——为什么，作为恶魔之神的撒旦人和人之间生下了孩子吗？“青い夜”是什么？当得知一切时，燐给出的答案是……。故事终于揭露了《青之驱魔师》的核心——', 'studio VOLN', '2025-01-26 12:03:31', '2025-02-12 22:02:20', '周一', '11:00:00', 'TV', '战斗 热血');
INSERT INTO `anime` VALUES (2, '我独自升级 第二季', 'anime02.jpg', '2024-01-04', '连载', '连接异次元和现世界的通道“门”突然出现的十来年后，世界上出现觉醒了超能力被称为“猎人”之人。猎人使用力量以攻占门内的地下城并获得报酬为生，但在强者齐聚的猎人们中，“水篠旬”作为被称为人类最弱兵器的低等猎人而艰难生活。某日，水篠旬遭遇某个被隐藏在低等级迷宫中的高等级重迷宫，在身负濒死重伤的水篠旬面前出现了神秘的探索窗口。他在临死之际，决定接受任务，成为唯一能“升级”之人。', 'A-1 Pictures', '2025-01-26 12:05:39', '2025-02-14 12:31:14', '周六', '12:00:00', 'TV', '热血 战斗 科幻');
INSERT INTO `anime` VALUES (3, '药屋少女的呢喃 第二季', 'anime03.jpg', '2024-01-10', '连载', '出身烟花巷的猫猫是善于推理和具丰富药理知识的少女，原本在烟花巷担任药师，但由于外出采药时遭人口贩子拐卖至后宫作下级侍女（宫女），意外被管理后宫的宦官壬氏发现其才能。凭借自身的学识和技能，猫猫在勾心斗角的宫廷中排忧解难、逐渐建立人脉对宫廷产生影响力，也发掘出宫廷里不为人知的一面。', 'TOHO animation STUDIO×OLM', '2025-01-26 14:03:17', '2025-01-28 16:05:14', '周五', '11:30:00', 'TV', '推理 日常');
INSERT INTO `anime` VALUES (4, '我和班上最讨厌的女生结婚了', 'anime04.jpg', '2025-01-04', '连载', '高中生北条才人和同班同学结婚了。这也是学校里最不擅长的，天敌般的女子樱森朱音。“和我结婚的事，跟班里的人说了会杀了你。” “我也不想让别人知道我结婚的事，没有什么好处。” 平时就互相讨厌的两人的新婚生活不可能顺利进行，总是和什么冲突。但是，在每天的生活中拉近彼此的距离，在一起的快乐时光增加，慢慢地相互理解。才人知道了至今为止都不知道的朱音可爱的素颜，朱音渐渐开始注意到了自己心中隐藏的想法……无法坦诚面对的两人所度过的令人心动的新婚生活，就此开幕——！', 'Studio五組×AXsiZ', '2025-01-26 14:03:28', '2025-01-28 16:42:09', '周六', '22:00:00', 'TV', '搞笑 校园 恋爱 日常');
INSERT INTO `anime` VALUES (5, '欢迎来到日本，精灵小姐。', 'anime05.jpg', '2025-01-10', '连载', '精灵小姐迷上日本文化！上班族北濑一广唯一的兴趣就是睡觉。他从小就在不可思议的梦中世界进行刺激的冒险。某次与在梦中结识的精灵一起冒险时，两人不幸被龙的吐息烧死。冒险失败后，他一如往常，从位于东京的房内醒来，却赫然发现精灵少女正熟睡在身旁', 'ゼロジー', '2025-01-26 14:03:40', '2025-01-28 16:17:19', '周五', '22:30:00', 'TV', '搞笑 恋爱 日常');
INSERT INTO `anime` VALUES (6, '蜂蜜柠檬碳酸水', 'anime06.jpg', '2025-01-08', '连载', '中学时代被叫做“石”的石森羽花，为了改变自己，进入校风自由的八美津高中就读。', 'J.C.STAFF', '2025-01-26 14:03:49', '2025-01-28 16:15:23', '周三', '19:00:00', 'TV', '校园 青春 恋爱');
INSERT INTO `anime` VALUES (7, '无名记忆 第二季', 'anime07.jpg', '2025-01-07', '连载', '这是一个拥有巨大力量的五位魔女被人们所畏惧的时代。大国法尔萨斯的王太子奥斯卡，为了解除年幼时被施加的“断子绝孙的诅咒”，拜访了居住在荒野高塔上的“苍月魔女”缇娜夏。传说登顶“魔女之塔”便能实现任何心愿，奥斯卡在通过试炼后许下的心愿是，让缇娜夏成为自己的妻子…… 成为魔女契约者的王太子，成为王太子守护者的魔女。二人的相遇为“魔女时代”带来变革，最终将会改写世界的命运。等待奥斯卡和缇娜夏的，将是一个怎样的故事', 'ENGI', '2025-01-26 19:40:41', '2025-01-28 16:20:48', '周二', '23:30:00', 'TV', '恋爱 科幻');
INSERT INTO `anime` VALUES (8, 'BanG Dream! 颂乐人偶', 'anime08.jpg', '2025-01-02', '连载', '“我不是说了吗？请把你剩余的人生交给我” 由丰川祥子招募成员的乐队Ave Mujica，', 'サンジゲン', '2025-01-26 19:40:41', '2025-01-28 16:38:13', '周四', '22:00:00', 'TV', '校园 励志');
INSERT INTO `anime` VALUES (9, 'Re：从零开始的异世界生活 第三季 反击篇', 'anime09.jpg', '2025-02-05', '连载', '击退了袭击而来的艾莎们的猛攻，在与大兔的战斗中完成了与比阿特丽斯的契约的“圣域”解放过了1年。面对王选的爱蜜莉雅阵营团结一致，充实地度过每一天的莱月昴，不过，因使者送来的一纸文书而宣告了平静的结束。那是国王候选人之一的阿纳斯塔西娅写给爱蜜莉雅的邀请函，邀请她前往卢格尼卡五大城市之一的水门城市普利斯特拉。接受了邀请，前往普利斯特拉的昴一行人等待着各种各样的再会。一个是意外的，一个是无意的，还有一个是应该来的。在水下蠢蠢欲动的恶意胎动和降临的前所未有的危机。少年再次面对残酷的命运。', 'WHITE FOX', '2025-01-26 19:40:41', '2025-02-12 16:38:13', '周三', '22:30:00', 'TV', '战斗 后宫 治愈');
INSERT INTO `anime` VALUES (10, '全修。', 'anime10.jpg', '2025-01-05', '连载', '高中毕业后怀抱着梦想成为动画师的广濑夏子终于踏入了动画制作公司的大门。她才华横溢并以惊人的速度完成了从新人到监督的蜕变。她的第一部监督作品更是引发了社会现象般的轰动，一举成名被誉为动画界的新进天才。然而她的下一部作品以初恋为主题的剧场版恋爱喜剧却遇到了瓶颈。从未体验过恋爱的夏子对初恋一无所知，无法将自己的情感融入创作。导致动画制作陷入了困境。', 'MAPPA', '2025-02-12 19:26:00', '2025-02-15 16:51:14', '周日', '22:45:00', 'TV', '运动 竞技');
INSERT INTO `anime` VALUES (11, 'BanG Dream! It\'s MyGO!!!!!', 'anime11.jpg', '2023-06-29', '完结', '“你一辈子都乐队吗？”高一的春天结束。羽丘女子学园里，每个人都有他的乐队，为了迟到入学的爱音也能尽快适应班级，急忙寻找乐队成员。这时，得知“羽丘的不可思议”灯还没有组成乐队，爱之音不知不觉地响起……。伤痕累，丑陋的，我们的<音乐呐喊>。无论迷路还是迷路，继续前进。', 'サンジゲン', '2025-02-12 19:26:11', '2025-02-15 16:42:48', NULL, NULL, 'TV', '励志 校园');
INSERT INTO `anime` VALUES (12, '樱花庄的宠物女孩', 'anime12.jpg', '2012-10-08', '完结', '电视动画《樱花庄的宠物女孩》改编自日本轻小说家鸭志田一原作的同名轻小说，水明艺术大学附属高中额外独立宿舍，通称“樱花庄”是群聚了一群充满各式问题学生入住的特殊公寓，被一般人作为“怪人的巢穴”看待。因无法抛弃弃猫而被赶出一般宿舍的主人公神田空太也是其中的一份子。本来应该以“脱出樱花庄”为目标的空太，却由于天才画家椎名真白的入住，导因于其连穿衣服都无法完全自行处理的“超”字辈等级的生活白痴水准，空太莫名奇妙的肩负起有如宠物般的“饲主”责任，开始了照顾真白和被其耍弄的哭笑不得的日子。', 'J.C.STAFF', '2025-02-15 16:54:11', '2025-02-15 16:54:11', NULL, NULL, 'TV', '励志 校园 治愈 青春');
INSERT INTO `anime` VALUES (13, '葬送的芙莉莲', 'anime13.jpg', '2023-09-29', '完结', '魔法使芙莉莲与勇者辛美尔等人在长达10年的冒险中打败了魔王，给世界带来了和平。作为一个活了一千多年的精灵，芙莉莲约定将与辛美尔等人再会，自己则先独自旅行。五十年后，芙莉莲拜访了辛美尔，但辛美尔已经老了，生命已所剩无几。之后，芙莉莲目睹了辛美尔的死亡，她也猛地意识到自己从未“了解人类”，并后悔不已，因此她踏上了“为了了解人类”的旅途。在那段旅途中，她邂逅了各种各样的人，经历了各种各样的事。', 'MADHOUSE', '2025-02-15 17:08:51', '2025-02-15 17:08:51', NULL, NULL, 'TV', '科幻');
INSERT INTO `anime` VALUES (14, '斗破苍穹三年之约', 'anime14.jpg', '2021-10-31', '完结', '三年前，云岚宗少宗主纳兰嫣然贸然退婚，倍感耻辱的萧炎遂与纳兰嫣然定下三年之约。为了这个约定，萧炎一直修炼不息，从魔兽山脉到塔戈尔沙漠的历练，从炼丹到吞噬异火的焚身之痛，都为证明那句话：“三十年河东，三十年河西，莫欺少年穷”。如今，萧家男儿必将迎难而上，永不退缩，萧炎的成长之路仍在继续……', '幻维数码', '2025-02-15 19:57:16', '2025-02-15 19:57:16', NULL, NULL, 'WEB', '科幻');
INSERT INTO `anime` VALUES (15, '特别篇 吹响！悠风号～合奏比赛～', 'anime15.jpg', '2023-08-04', '完结', '新世代，开启！这是由京都动画所描绘的、 将梦想寄托于吹奏乐上的高中生们那微不足道却又“无比特别\"的青春篇章！本次中篇动画改编自武田绫乃所著小说「吹响吧！上低音号 北宇治高中吹奏部的真实故事」（宝岛社出版）中的人气章节！吹奏乐强校的吹奏部新部长所接到的第一份工作竟是部内“调整\"！？北宇治吹奏部，共计65人。故事就在主人公——久美子作为部长，处理部员们接连不断的咨询和突然爆发的矛盾中拉开帷幕。她能顺利地完成这第一份工作吗——？调音，完成！', '京都アニメーション', '2025-02-15 20:05:35', '2025-02-15 20:05:35', NULL, NULL, '剧场版', '校园 青春 ');
INSERT INTO `anime` VALUES (16, '不时用俄语小声说真心话的邻桌艾莉同学', 'anime16.jpg', '2024-07-03', '完结', '坐在久世政附近旁边的艾莉亚总是冷地看着他。有时候，我用俄语跟他说话。……。政近也不会漏听这句话。何况，政近的俄语听力是母语水平！我以为你没有注意到，有时会打扰阿莉亚先生。而且，政近虽然明白了意思，却装作没有察觉的样子。笑不完，两人的恋情走向——！？', '動画工房', '2025-02-15 20:10:39', '2025-02-15 20:10:39', NULL, NULL, 'TV', '校园 恋爱');
INSERT INTO `anime` VALUES (17, '孤独摇滚！', 'anime17.jpg', '2022-10-08', '完结', '在19日发售的《MANGA TIME KIRARA MAX 4月号》（芳文社）上，漫画《孤独摇滚》决定了TV动画化的消息。《孤独摇滚》是一部以在视频网站作为“吉他英雄”活跃，却在现实生活中没有朋友的女高中生后藤一人为主角展开的音乐漫画主要讲述了机缘巧合下加入了由伊地知虹夏所率领的团结乐队后发生的故事，在新环境她将面对的是在众人面前不习惯演奏带来的极大的挑战。日前作品单行本已经刊行两卷，最新的第三卷将于2月25日发售。', 'CloverWorks', '2025-02-15 20:12:53', '2025-02-15 20:12:53', NULL, NULL, 'TV', '励志 运动');

-- ----------------------------
-- Table structure for anime_category
-- ----------------------------
DROP TABLE IF EXISTS `anime_category`;
CREATE TABLE `anime_category`  (
  `anime_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`anime_id`, `category_id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `anime_category_ibfk_1` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `anime_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of anime_category
-- ----------------------------
INSERT INTO `anime_category` VALUES (1, 2);
INSERT INTO `anime_category` VALUES (3, 2);
INSERT INTO `anime_category` VALUES (4, 2);
INSERT INTO `anime_category` VALUES (5, 2);
INSERT INTO `anime_category` VALUES (6, 2);
INSERT INTO `anime_category` VALUES (7, 2);
INSERT INTO `anime_category` VALUES (8, 2);
INSERT INTO `anime_category` VALUES (9, 2);
INSERT INTO `anime_category` VALUES (10, 2);
INSERT INTO `anime_category` VALUES (11, 2);
INSERT INTO `anime_category` VALUES (12, 2);
INSERT INTO `anime_category` VALUES (13, 2);
INSERT INTO `anime_category` VALUES (15, 2);
INSERT INTO `anime_category` VALUES (16, 2);
INSERT INTO `anime_category` VALUES (17, 2);
INSERT INTO `anime_category` VALUES (14, 3);
INSERT INTO `anime_category` VALUES (2, 4);
INSERT INTO `anime_category` VALUES (1, 6);
INSERT INTO `anime_category` VALUES (2, 6);
INSERT INTO `anime_category` VALUES (4, 6);
INSERT INTO `anime_category` VALUES (5, 6);
INSERT INTO `anime_category` VALUES (6, 6);
INSERT INTO `anime_category` VALUES (7, 6);
INSERT INTO `anime_category` VALUES (8, 6);
INSERT INTO `anime_category` VALUES (9, 6);
INSERT INTO `anime_category` VALUES (10, 6);
INSERT INTO `anime_category` VALUES (11, 6);
INSERT INTO `anime_category` VALUES (12, 6);
INSERT INTO `anime_category` VALUES (13, 6);
INSERT INTO `anime_category` VALUES (15, 7);
INSERT INTO `anime_category` VALUES (16, 7);
INSERT INTO `anime_category` VALUES (17, 7);
INSERT INTO `anime_category` VALUES (8, 10);
INSERT INTO `anime_category` VALUES (11, 10);
INSERT INTO `anime_category` VALUES (16, 10);
INSERT INTO `anime_category` VALUES (14, 12);
INSERT INTO `anime_category` VALUES (6, 14);
INSERT INTO `anime_category` VALUES (5, 15);
INSERT INTO `anime_category` VALUES (1, 24);
INSERT INTO `anime_category` VALUES (10, 24);
INSERT INTO `anime_category` VALUES (9, 25);
INSERT INTO `anime_category` VALUES (15, 27);
INSERT INTO `anime_category` VALUES (2, 30);
INSERT INTO `anime_category` VALUES (4, 30);
INSERT INTO `anime_category` VALUES (7, 30);
INSERT INTO `anime_category` VALUES (3, 32);
INSERT INTO `anime_category` VALUES (12, 32);
INSERT INTO `anime_category` VALUES (13, 33);
INSERT INTO `anime_category` VALUES (1, 40);
INSERT INTO `anime_category` VALUES (2, 40);
INSERT INTO `anime_category` VALUES (5, 40);
INSERT INTO `anime_category` VALUES (6, 40);
INSERT INTO `anime_category` VALUES (7, 40);
INSERT INTO `anime_category` VALUES (8, 40);
INSERT INTO `anime_category` VALUES (9, 40);
INSERT INTO `anime_category` VALUES (10, 40);
INSERT INTO `anime_category` VALUES (3, 41);
INSERT INTO `anime_category` VALUES (11, 41);
INSERT INTO `anime_category` VALUES (13, 42);
INSERT INTO `anime_category` VALUES (15, 42);
INSERT INTO `anime_category` VALUES (16, 42);
INSERT INTO `anime_category` VALUES (17, 42);
INSERT INTO `anime_category` VALUES (12, 43);
INSERT INTO `anime_category` VALUES (14, 43);
INSERT INTO `anime_category` VALUES (1, 44);
INSERT INTO `anime_category` VALUES (2, 44);
INSERT INTO `anime_category` VALUES (3, 44);
INSERT INTO `anime_category` VALUES (5, 44);
INSERT INTO `anime_category` VALUES (6, 44);
INSERT INTO `anime_category` VALUES (7, 44);
INSERT INTO `anime_category` VALUES (8, 44);
INSERT INTO `anime_category` VALUES (9, 44);
INSERT INTO `anime_category` VALUES (10, 44);
INSERT INTO `anime_category` VALUES (11, 45);
INSERT INTO `anime_category` VALUES (12, 45);
INSERT INTO `anime_category` VALUES (13, 45);
INSERT INTO `anime_category` VALUES (14, 45);
INSERT INTO `anime_category` VALUES (15, 45);
INSERT INTO `anime_category` VALUES (16, 45);
INSERT INTO `anime_category` VALUES (17, 45);
INSERT INTO `anime_category` VALUES (4, 47);
INSERT INTO `anime_category` VALUES (5, 47);
INSERT INTO `anime_category` VALUES (8, 49);
INSERT INTO `anime_category` VALUES (1, 50);
INSERT INTO `anime_category` VALUES (2, 50);
INSERT INTO `anime_category` VALUES (1, 51);
INSERT INTO `anime_category` VALUES (2, 51);
INSERT INTO `anime_category` VALUES (9, 51);
INSERT INTO `anime_category` VALUES (4, 53);
INSERT INTO `anime_category` VALUES (6, 53);
INSERT INTO `anime_category` VALUES (8, 53);
INSERT INTO `anime_category` VALUES (6, 54);
INSERT INTO `anime_category` VALUES (4, 55);
INSERT INTO `anime_category` VALUES (5, 55);
INSERT INTO `anime_category` VALUES (6, 55);
INSERT INTO `anime_category` VALUES (7, 55);
INSERT INTO `anime_category` VALUES (9, 56);
INSERT INTO `anime_category` VALUES (9, 57);
INSERT INTO `anime_category` VALUES (3, 58);
INSERT INTO `anime_category` VALUES (2, 59);
INSERT INTO `anime_category` VALUES (7, 59);
INSERT INTO `anime_category` VALUES (17, 60);

-- ----------------------------
-- Table structure for anime_comments
-- ----------------------------
DROP TABLE IF EXISTS `anime_comments`;
CREATE TABLE `anime_comments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `anime_id` int NOT NULL,
  `user_id` int NOT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `anime_id`(`anime_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `anime_comments_ibfk_1` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `anime_comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of anime_comments
-- ----------------------------
INSERT INTO `anime_comments` VALUES (1, 8, 1, '你好', '2025-02-14 16:32:42');
INSERT INTO `anime_comments` VALUES (4, 2, 1, '我独自升级真好看', '2025-02-14 17:38:10');
INSERT INTO `anime_comments` VALUES (5, 8, 2, '真不错', '2025-02-14 17:38:48');
INSERT INTO `anime_comments` VALUES (6, 8, 2, '你好', '2025-02-14 17:40:13');
INSERT INTO `anime_comments` VALUES (9, 8, 2, 'hao', '2025-02-14 17:40:23');
INSERT INTO `anime_comments` VALUES (10, 8, 2, '好', '2025-02-14 17:40:28');
INSERT INTO `anime_comments` VALUES (11, 8, 2, '好', '2025-02-14 17:40:37');
INSERT INTO `anime_comments` VALUES (12, 8, 2, '好', '2025-02-14 17:40:40');
INSERT INTO `anime_comments` VALUES (13, 8, 2, '好', '2025-02-14 17:40:42');
INSERT INTO `anime_comments` VALUES (14, 3, 4, '测试评论', '2025-02-14 20:04:02');
INSERT INTO `anime_comments` VALUES (15, 9, 2, '测试评论', '2025-02-14 20:07:02');
INSERT INTO `anime_comments` VALUES (16, 8, 2, '测试', '2025-02-15 14:16:46');

-- ----------------------------
-- Table structure for anime_episode
-- ----------------------------
DROP TABLE IF EXISTS `anime_episode`;
CREATE TABLE `anime_episode`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `anime_id` int NOT NULL COMMENT '关联的动漫ID',
  `episode_number` int NOT NULL COMMENT '集数',
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '本集标题',
  `video_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '视频链接',
  `duration` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '时长',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `anime_id`(`anime_id` ASC) USING BTREE,
  CONSTRAINT `anime_episode_ibfk_1` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of anime_episode
-- ----------------------------
INSERT INTO `anime_episode` VALUES (1, 1, 1, '第1集', 'local:anime1_ep1.mp4', '23:45', '2025-01-06 12:15:24');
INSERT INTO `anime_episode` VALUES (2, 1, 2, '第2集', 'local:anime1_ep2.mp4', '23:45', '2025-01-13 12:15:24');
INSERT INTO `anime_episode` VALUES (3, 2, 1, '第1集', 'local:anime2_ep1.mp4', '23:45', '2025-01-04 12:15:24');
INSERT INTO `anime_episode` VALUES (4, 2, 2, '第2集', 'local:anime2_ep2.mp4', '23:45', '2025-01-11 12:15:24');
INSERT INTO `anime_episode` VALUES (5, 2, 3, '第3集', 'local:anime2_ep3.mp4', '23:45', '2025-01-18 14:22:33');
INSERT INTO `anime_episode` VALUES (6, 2, 4, '第4集', 'local:anime2_ep4.mp4', '23:45', '2025-01-25 21:58:06');
INSERT INTO `anime_episode` VALUES (7, 2, 5, '第5集', 'local:anime2_ep5.mp4', '23:45', '2025-02-01 21:58:50');
INSERT INTO `anime_episode` VALUES (8, 2, 6, '第6集', 'local:anime2_ep6.mp4', '23:45', '2025-02-08 21:59:39');
INSERT INTO `anime_episode` VALUES (9, 1, 3, '第3集', 'local:anime1_ep3.mp4', '23:45', '2025-01-20 22:05:11');
INSERT INTO `anime_episode` VALUES (10, 1, 4, '第4集', 'local:anime1_ep4.mp4', '23:45', '2025-01-27 22:06:15');
INSERT INTO `anime_episode` VALUES (11, 1, 5, '第5集', 'local:anime1_ep5.mp4', '23:45', '2025-02-03 22:07:04');
INSERT INTO `anime_episode` VALUES (12, 1, 6, '第6集', 'local:anime1_ep6.mp4', '23:45', '2025-02-10 21:58:06');
INSERT INTO `anime_episode` VALUES (13, 3, 1, '第1集', 'local:anime3_ep1.mp4', '24:00', '2025-01-10 22:12:17');
INSERT INTO `anime_episode` VALUES (14, 3, 2, '第2集', 'local:anime3_ep2.mp4', '24:00', '2025-01-17 22:17:02');
INSERT INTO `anime_episode` VALUES (15, 3, 3, '第3集', 'local:anime3_ep3.mp4', '24:00', '2025-01-24 22:17:02');
INSERT INTO `anime_episode` VALUES (16, 3, 4, '第4集', 'local:anime3_ep4.mp4', '24:00', '2025-01-31 22:17:02');
INSERT INTO `anime_episode` VALUES (17, 3, 5, '第5集', 'local:anime3_ep5.mp4', '24:00', '2025-02-07 22:17:02');
INSERT INTO `anime_episode` VALUES (18, 4, 1, '第1集', 'local:anime4_ep1.mp4', '23:40', '2025-01-04 22:27:59');
INSERT INTO `anime_episode` VALUES (19, 4, 2, '第2集', 'local:anime4_ep2.mp4', '23:40', '2025-01-11 22:29:10');
INSERT INTO `anime_episode` VALUES (20, 4, 3, '第3集', 'local:anime4_ep3.mp4', '23:40', '2025-01-18 22:30:47');
INSERT INTO `anime_episode` VALUES (21, 4, 4, '第4集', 'local:anime4_ep4.mp4', '23:40', '2025-01-25 22:30:47');
INSERT INTO `anime_episode` VALUES (22, 4, 5, '第5集', 'local:anime4_ep5.mp4', '23:40', '2025-02-01 22:31:41');
INSERT INTO `anime_episode` VALUES (23, 4, 6, '第6集', 'local:anime4_ep6.mp4', '23:40', '2025-02-08 22:31:41');
INSERT INTO `anime_episode` VALUES (24, 5, 1, '第1集', 'local:anime5_ep1.mp4', '23:39', '2025-01-10 23:02:17');
INSERT INTO `anime_episode` VALUES (25, 5, 2, '第2集', 'local:anime5_ep2.mp4', '23:40', '2025-01-17 23:03:13');
INSERT INTO `anime_episode` VALUES (26, 5, 3, '第3集', 'local:anime5_ep3.mp4', '23:40', '2025-01-24 23:03:13');
INSERT INTO `anime_episode` VALUES (27, 5, 4, '第4集', 'local:anime5_ep4.mp4', '23:40', '2025-01-31 23:04:03');
INSERT INTO `anime_episode` VALUES (28, 5, 5, '第5集', 'local:anime5_ep5.mp4', '23:40', '2025-02-07 23:04:03');
INSERT INTO `anime_episode` VALUES (29, 8, 1, '第1集', 'local:anime8_ep1.mp4', '23:40', '2025-01-02 23:08:29');
INSERT INTO `anime_episode` VALUES (30, 8, 2, '第2集', 'local:anime8_ep2.mp4', '23:40', '2025-01-09 23:08:29');
INSERT INTO `anime_episode` VALUES (31, 8, 3, '第3集', 'local:anime8_ep3.mp4', '23:40', '2025-01-16 23:08:29');
INSERT INTO `anime_episode` VALUES (32, 8, 4, '第4集', 'local:anime8_ep4.mp4', '23:40', '2025-01-23 23:08:29');
INSERT INTO `anime_episode` VALUES (33, 8, 5, '第5集', 'local:anime8_ep5.mp4', '23:40', '2025-01-30 23:08:29');
INSERT INTO `anime_episode` VALUES (34, 8, 6, '第6集', 'local:anime8_ep6.mp4', '23:29', '2025-02-06 23:08:29');
INSERT INTO `anime_episode` VALUES (35, 6, 1, '第1集', 'local:anime6_ep1.mp4', '22:55', '2025-01-08 23:23:38');
INSERT INTO `anime_episode` VALUES (36, 6, 2, '第2集', 'local:anime6_ep2.mp4', '22:55', '2025-01-15 23:23:38');
INSERT INTO `anime_episode` VALUES (37, 6, 3, '第3集', 'local:anime6_ep3.mp4', '22:55', '2025-01-22 23:23:38');
INSERT INTO `anime_episode` VALUES (38, 6, 4, '第4集', 'local:anime6_ep4.mp4', '22:55', '2025-01-29 23:23:38');
INSERT INTO `anime_episode` VALUES (39, 6, 5, '第5集', 'local:anime6_ep5.mp4', '22:55', '2025-02-05 23:23:38');
INSERT INTO `anime_episode` VALUES (40, 7, 1, '第1集', 'local:anime7_ep1.mp4', '23:40', '2025-01-07 23:28:27');
INSERT INTO `anime_episode` VALUES (41, 7, 2, '第2集', 'local:anime7_ep2.mp4', '23:40', '2025-01-14 23:28:27');
INSERT INTO `anime_episode` VALUES (42, 7, 3, '第3集', 'local:anime7_ep3.mp4', '23:40', '2025-01-21 23:28:27');
INSERT INTO `anime_episode` VALUES (43, 7, 4, '第4集', 'local:anime7_ep4.mp4', '23:40', '2025-01-28 23:28:27');
INSERT INTO `anime_episode` VALUES (44, 7, 5, '第5集', 'local:anime7_ep5.mp4', '23:40', '2025-02-04 23:28:27');
INSERT INTO `anime_episode` VALUES (45, 7, 6, '第6集', 'local:anime7_ep6.mp4', '23:37', '2025-02-11 23:28:27');
INSERT INTO `anime_episode` VALUES (46, 9, 1, '第1集', 'local:anime9_ep1.mp4', '23:40', '2025-02-05 23:38:57');
INSERT INTO `anime_episode` VALUES (47, 9, 2, '第2集', 'local:anime9_ep2.mp4', '23:40', '2025-02-12 23:38:57');
INSERT INTO `anime_episode` VALUES (48, 8, 7, '第7集', 'local:anime8_ep7.mp4', '23:40', '2025-02-13 11:56:44');
INSERT INTO `anime_episode` VALUES (49, 10, 1, '第1集', 'local:anime10_ep1.mp4', '23:55', '2025-01-05 12:05:09');
INSERT INTO `anime_episode` VALUES (50, 10, 2, '第2集', 'local:anime10_ep2.mp4', '23:55', '2025-01-12 12:05:09');
INSERT INTO `anime_episode` VALUES (51, 10, 3, '第3集', 'local:anime10_ep3.mp4', '23:55', '2025-01-19 12:05:09');
INSERT INTO `anime_episode` VALUES (52, 10, 4, '第4集', 'local:anime10_ep4.mp4', '23:55', '2025-01-26 12:05:09');
INSERT INTO `anime_episode` VALUES (53, 10, 5, '第5集', 'local:anime10_ep5.mp4', '23:55', '2025-02-02 12:05:09');
INSERT INTO `anime_episode` VALUES (54, 10, 6, '第6集', 'local:anime10_ep6.mp4', '23:55', '2025-02-09 12:05:09');

-- ----------------------------
-- Table structure for anime_tag_weights
-- ----------------------------
DROP TABLE IF EXISTS `anime_tag_weights`;
CREATE TABLE `anime_tag_weights`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `anime_id` int NOT NULL,
  `tag_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `weight` decimal(5, 2) NULL DEFAULT 1.00 COMMENT '标签权重',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_anime_tag`(`anime_id` ASC, `tag_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of anime_tag_weights
-- ----------------------------
INSERT INTO `anime_tag_weights` VALUES (1, 1, '战斗 热血', 1.00);
INSERT INTO `anime_tag_weights` VALUES (2, 2, '热血 战斗 科幻', 1.00);
INSERT INTO `anime_tag_weights` VALUES (3, 3, '推理 日常', 1.00);
INSERT INTO `anime_tag_weights` VALUES (4, 4, '搞笑 校园 恋爱 日常', 1.00);
INSERT INTO `anime_tag_weights` VALUES (5, 5, '搞笑 恋爱 日常', 1.00);
INSERT INTO `anime_tag_weights` VALUES (6, 6, '校园 青春 恋爱', 1.00);
INSERT INTO `anime_tag_weights` VALUES (7, 7, '恋爱 科幻', 1.00);
INSERT INTO `anime_tag_weights` VALUES (8, 8, '校园 励志', 1.00);
INSERT INTO `anime_tag_weights` VALUES (9, 9, '战斗 后宫 治愈', 1.00);
INSERT INTO `anime_tag_weights` VALUES (10, 10, '运动 竞技', 1.00);
INSERT INTO `anime_tag_weights` VALUES (11, 11, '励志 校园', 1.00);
INSERT INTO `anime_tag_weights` VALUES (12, 12, '励志 校园 治愈 青春', 1.00);
INSERT INTO `anime_tag_weights` VALUES (13, 13, '科幻', 1.00);
INSERT INTO `anime_tag_weights` VALUES (14, 14, '科幻', 1.00);
INSERT INTO `anime_tag_weights` VALUES (15, 15, '校园 青春 ', 1.00);
INSERT INTO `anime_tag_weights` VALUES (16, 16, '校园 恋爱', 1.00);
INSERT INTO `anime_tag_weights` VALUES (17, 17, '励志 运动', 1.00);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `type` enum('region','format','letter','year','season','status','genre') CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '分类类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (2, '日本', 'region');
INSERT INTO `category` VALUES (3, '中国', 'region');
INSERT INTO `category` VALUES (4, '欧美', 'region');
INSERT INTO `category` VALUES (6, 'TV', 'format');
INSERT INTO `category` VALUES (7, '剧场版', 'format');
INSERT INTO `category` VALUES (8, 'OVA', 'format');
INSERT INTO `category` VALUES (9, 'A', 'letter');
INSERT INTO `category` VALUES (10, 'B', 'letter');
INSERT INTO `category` VALUES (11, 'C', 'letter');
INSERT INTO `category` VALUES (12, 'D', 'letter');
INSERT INTO `category` VALUES (13, 'E', 'letter');
INSERT INTO `category` VALUES (14, 'F', 'letter');
INSERT INTO `category` VALUES (15, 'H', 'letter');
INSERT INTO `category` VALUES (16, 'I', 'letter');
INSERT INTO `category` VALUES (17, 'J', 'letter');
INSERT INTO `category` VALUES (18, 'K', 'letter');
INSERT INTO `category` VALUES (19, 'L', 'letter');
INSERT INTO `category` VALUES (20, 'M', 'letter');
INSERT INTO `category` VALUES (21, 'N', 'letter');
INSERT INTO `category` VALUES (22, 'O', 'letter');
INSERT INTO `category` VALUES (23, 'P', 'letter');
INSERT INTO `category` VALUES (24, 'Q', 'letter');
INSERT INTO `category` VALUES (25, 'R', 'letter');
INSERT INTO `category` VALUES (26, 'S', 'letter');
INSERT INTO `category` VALUES (27, 'T', 'letter');
INSERT INTO `category` VALUES (28, 'U', 'letter');
INSERT INTO `category` VALUES (29, 'V', 'letter');
INSERT INTO `category` VALUES (30, 'W', 'letter');
INSERT INTO `category` VALUES (31, 'X', 'letter');
INSERT INTO `category` VALUES (32, 'Y', 'letter');
INSERT INTO `category` VALUES (33, 'Z', 'letter');
INSERT INTO `category` VALUES (40, '1月', 'season');
INSERT INTO `category` VALUES (41, '4月', 'season');
INSERT INTO `category` VALUES (42, '7月', 'season');
INSERT INTO `category` VALUES (43, '10月', 'season');
INSERT INTO `category` VALUES (44, '连载', 'status');
INSERT INTO `category` VALUES (45, '完结', 'status');
INSERT INTO `category` VALUES (46, '未播放', 'status');
INSERT INTO `category` VALUES (47, '搞笑', 'genre');
INSERT INTO `category` VALUES (48, '运动', 'genre');
INSERT INTO `category` VALUES (49, '励志', 'genre');
INSERT INTO `category` VALUES (50, '热血', 'genre');
INSERT INTO `category` VALUES (51, '战斗', 'genre');
INSERT INTO `category` VALUES (52, '竞技', 'genre');
INSERT INTO `category` VALUES (53, '校园', 'genre');
INSERT INTO `category` VALUES (54, '青春', 'genre');
INSERT INTO `category` VALUES (55, '恋爱', 'genre');
INSERT INTO `category` VALUES (56, '后宫', 'genre');
INSERT INTO `category` VALUES (57, '治愈', 'genre');
INSERT INTO `category` VALUES (58, '推理', 'genre');
INSERT INTO `category` VALUES (59, '科幻', 'genre');
INSERT INTO `category` VALUES (60, 'G', 'letter');

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `anime_id` int NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_anime`(`user_id` ASC, `anime_id` ASC) USING BTREE,
  INDEX `anime_id`(`anime_id` ASC) USING BTREE,
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (4, 2, 7, '2025-02-15 15:12:54');
INSERT INTO `favorite` VALUES (5, 2, 10, '2025-02-15 15:12:57');
INSERT INTO `favorite` VALUES (6, 2, 8, '2025-02-15 15:13:00');
INSERT INTO `favorite` VALUES (7, 2, 5, '2025-02-15 15:13:02');
INSERT INTO `favorite` VALUES (8, 2, 4, '2025-02-15 15:13:15');
INSERT INTO `favorite` VALUES (9, 2, 6, '2025-02-15 15:13:17');
INSERT INTO `favorite` VALUES (12, 2, 11, '2025-02-15 16:43:31');
INSERT INTO `favorite` VALUES (14, 1, 8, '2025-02-18 21:02:38');
INSERT INTO `favorite` VALUES (15, 1, 9, '2025-03-06 12:50:40');

-- ----------------------------
-- Table structure for recommendations
-- ----------------------------
DROP TABLE IF EXISTS `recommendations`;
CREATE TABLE `recommendations`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `anime_id` int NOT NULL,
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '推荐分数',
  `reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '推荐原因',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `is_shown` int NULL DEFAULT 0 COMMENT '是否已展示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_anime`(`user_id` ASC, `anime_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recommendations
-- ----------------------------
INSERT INTO `recommendations` VALUES (29, 1, 11, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);
INSERT INTO `recommendations` VALUES (30, 1, 12, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);
INSERT INTO `recommendations` VALUES (31, 1, 13, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);
INSERT INTO `recommendations` VALUES (32, 1, 14, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);
INSERT INTO `recommendations` VALUES (33, 1, 15, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);
INSERT INTO `recommendations` VALUES (34, 1, 16, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);
INSERT INTO `recommendations` VALUES (35, 1, 17, NULL, '根据热门度推荐', '2025-03-06 12:23:02', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456');
INSERT INTO `user` VALUES (2, 'zhangsan', '1234567');
INSERT INTO `user` VALUES (3, 'admin1', '123456');
INSERT INTO `user` VALUES (4, 'xiejinxin', '12345678');

-- ----------------------------
-- Table structure for user_preferences
-- ----------------------------
DROP TABLE IF EXISTS `user_preferences`;
CREATE TABLE `user_preferences`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `tag_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `weight` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '兴趣权重',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_tag`(`user_id` ASC, `tag_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_preferences
-- ----------------------------
INSERT INTO `user_preferences` VALUES (5, 1, '校园 励志', 0.33, '2025-03-06 13:13:04');
INSERT INTO `user_preferences` VALUES (6, 1, '热血 战斗 科幻', 0.33, '2025-03-06 13:13:04');
INSERT INTO `user_preferences` VALUES (7, 1, '运动 竞技', 0.33, '2025-03-06 13:13:04');

-- ----------------------------
-- Table structure for watch_history
-- ----------------------------
DROP TABLE IF EXISTS `watch_history`;
CREATE TABLE `watch_history`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `anime_id` int NOT NULL,
  `episode_id` int NOT NULL,
  `episode_number` int NOT NULL,
  `watch_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `watch_duration` int NULL DEFAULT NULL COMMENT '观看时长(秒)',
  `total_duration` int NULL DEFAULT NULL COMMENT '视频总时长(秒)',
  `watch_progress` decimal(5, 2) NULL DEFAULT NULL COMMENT '观看进度(百分比)',
  `is_finished` int NULL DEFAULT 0 COMMENT '是否看完',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_anime`(`user_id` ASC, `anime_id` ASC) USING BTREE,
  INDEX `idx_watch_time`(`watch_time` ASC) USING BTREE,
  INDEX `anime_id`(`anime_id` ASC) USING BTREE,
  INDEX `episode_id`(`episode_id` ASC) USING BTREE,
  CONSTRAINT `watch_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `watch_history_ibfk_2` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `watch_history_ibfk_3` FOREIGN KEY (`episode_id`) REFERENCES `anime_episode` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of watch_history
-- ----------------------------
INSERT INTO `watch_history` VALUES (37, 3, 4, 19, 2, '2025-02-16 00:30:28', NULL, NULL, NULL, 0);
INSERT INTO `watch_history` VALUES (42, 3, 6, 39, 5, '2025-02-16 00:31:16', NULL, NULL, NULL, 0);
INSERT INTO `watch_history` VALUES (44, 3, 2, 5, 3, '2025-02-16 00:31:24', NULL, NULL, NULL, 0);
INSERT INTO `watch_history` VALUES (73, 1, 2, 6, 4, '2025-03-06 12:23:13', NULL, NULL, NULL, 0);
INSERT INTO `watch_history` VALUES (75, 1, 10, 54, 6, '2025-03-06 12:23:25', NULL, NULL, NULL, 0);
INSERT INTO `watch_history` VALUES (77, 1, 8, 48, 7, '2025-03-06 12:24:04', NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
