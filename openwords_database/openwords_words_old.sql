SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `languages`
--

CREATE TABLE `languages` (
`language_id` int(11) NOT NULL,
  `language` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `meta_info` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `display_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `languages`
--

INSERT INTO `languages` (`language_id`, `language`, `code`, `meta_info`, `display_name`) VALUES
(1, '[english]', 'en', '', ''),
(2, '[abau]', 'aau', '', ''),
(3, '[abkhaz]', 'ab', '', ''),
(4, '[abenaki]', 'abe', '', ''),
(5, '[abaza]', 'abq', '', ''),
(6, '[acehnese]', 'ace', '', ''),
(7, '[adyghe]', 'ady', '', ''),
(8, '[avestan]', 'ae', '', ''),
(9, '[afrikaans]', 'af', '', ''),
(10, '[angor]', 'agg', '', ''),
(11, '[aghul]', 'agx', '', ''),
(12, '[ainu]', 'ain', '', ''),
(13, '[akan]', 'ak', '', ''),
(14, '[akkadian]', 'akk', '', ''),
(15, '[akhvakh]', 'akv', '', ''),
(16, '[alabama]', 'akz', '', ''),
(17, '[aleut]', 'ale', '', ''),
(18, '[alune]', 'alp', '', ''),
(19, '[southern altai]', 'alt', '', ''),
(20, '[amharic]', 'am', '', ''),
(21, '[ama (new guinea)]', 'amm', '', ''),
(22, '[amuzgo]', 'amu', '', ''),
(23, '[aragonese]', 'an', '', ''),
(24, '[old english]', 'ang', '', ''),
(25, '[andi]', 'ani', '', ''),
(26, '[uab meto]', 'aoz', '', ''),
(27, '[jicarilla]', 'apj', '', ''),
(28, '[lipan]', 'apl', '', ''),
(29, '[chiricahua]', 'apm', '', ''),
(30, '[apache#western apache][western apache]', 'apw', '', ''),
(31, '[arabic][arabic#libyan]', 'ar', '', ''),
(32, '[aramaic#syriac][aramaic#hebrew]', 'arc', '', ''),
(33, '[mapudungun]', 'arn', '', ''),
(34, '[arapaho]', 'arp', '', ''),
(35, '[arabic#moroccan arabic]', 'ary', '', ''),
(36, '[arabic#egyptian arabic]', 'arz', '', ''),
(37, '[assamese]', 'as', '', ''),
(38, '[assiniboine]', 'asb', '', ''),
(39, '[american sign language]', 'ase', '', ''),
(40, '[asturian]', 'ast', '', ''),
(41, '[atikamekw]', 'atj', '', ''),
(42, '[avar]', 'av', '', ''),
(43, '[alviri-vidari]', 'avd', '', ''),
(44, '[aymara]', 'ay', '', ''),
(45, '[azeri]', 'az', '', ''),
(46, '[bashkir]', 'ba', '', ''),
(47, '[baluchi]', 'bal', '', ''),
(48, '[balinese]', 'ban', '', ''),
(49, '[bavarian]', 'bar', '', ''),
(50, '[batak toba]', 'bbc', '', ''),
(51, '[budukh]', 'bdk', '', ''),
(52, '[west coast bajau]', 'bdr', '', ''),
(53, '[bandjalang]', 'bdy', '', ''),
(54, '[belarusian]', 'be', '', ''),
(55, '[beja]', 'bej', '', ''),
(56, '[bemba]', 'bem', '', ''),
(57, '[bulgarian]', 'bg', '', ''),
(58, '[banggai]', 'bgz', '', ''),
(59, '[banjarese]', 'bjn', '', ''),
(60, '[blackfoot]', 'bla', '', ''),
(61, '[bambara]', 'bm', '', ''),
(62, '[bengali]', 'bn', '', ''),
(63, '[bontoc]', 'bnc', '', ''),
(64, '[banda (indonesia)]', 'bnd', '', ''),
(65, '[bunun]', 'bnn', '', ''),
(66, '[bintulu]', 'bny', '', ''),
(67, '[tibetan]', 'bo', '', ''),
(68, '[borôro]', 'bor', '', ''),
(69, '[botlikh]', 'bph', '', ''),
(70, '[bilba]', 'bpz', '', ''),
(71, '[breton]', 'br', '', ''),
(72, '[brunei bisaya]', 'bsb', '', ''),
(73, '[buryat]', 'bua', '', ''),
(74, '[buginese]', 'bug', '', ''),
(75, '[catalan]', 'ca', '', ''),
(76, '[ch''orti'']', 'caa', '', ''),
(77, '[caddo]', 'cad', '', ''),
(78, '[chamicuro]', 'ccc', '', ''),
(79, '[chakma]', 'ccp', '', ''),
(80, '[chechen]', 'ce', '', ''),
(81, '[cebuano]', 'ceb', '', ''),
(82, '[gaulish]', 'cel-gau', '', ''),
(83, '[chamorro]', 'ch', '', ''),
(84, '[catawba]', 'chc', '', ''),
(85, '[chagatai]', 'chg', '', ''),
(86, '[cahuilla]', 'chl', '', ''),
(87, '[eastern mari]', 'chm', '', ''),
(88, '[choctaw]', 'cho', '', ''),
(89, '[cherokee]', 'chr', '', ''),
(90, '[cheyenne]', 'chy', '', ''),
(91, '[cia-cia]', 'cia', '', ''),
(92, '[chru]', 'cje', '', ''),
(93, '[chamalal]', 'cji', '', ''),
(94, '[shor]', 'cjs', '', ''),
(95, '[kurdish#sorani]', 'ckb', '', ''),
(96, '[chukchi]', 'ckt', '', ''),
(97, '[klallam]', 'clm', '', ''),
(98, '[chinese#wu][chinese#mandarin]', 'cmn', '', ''),
(99, '[corsican]', 'co', '', ''),
(100, '[comanche]', 'com', '', ''),
(101, '[coptic]', 'cop', '', ''),
(102, '[cree]', 'cr', '', ''),
(103, '[crimean tatar]', 'crh', '', ''),
(104, '[cora]', 'crn', '', ''),
(105, '[carrier]', 'crx', '', ''),
(106, '[czech]', 'cs', '', ''),
(107, '[kashubian]', 'csb', '', ''),
(108, '[old church slavonic][old church slavonic#cyrillic]', 'cu', '', ''),
(109, '[kuna]', 'cuk', '', ''),
(110, '[chuvash]', 'cv', '', ''),
(111, '[welsh]', 'cy', '', ''),
(112, '[danish]', 'da', '', ''),
(113, '[dargwa]', 'dar', '', ''),
(114, '[dyirbal]', 'dbl', '', ''),
(115, '[tsez]', 'ddo', '', ''),
(116, '[german]', 'de', '', ''),
(117, '[casiguran dumagat agta]', 'dgc', '', ''),
(118, '[dogrib]', 'dgr', '', ''),
(119, '[dalmatian]', 'dlm', '', ''),
(120, '[chinese#dungan]', 'dng', '', ''),
(121, '[sorbian#lower sorbian]', 'dsb', '', ''),
(122, '[dhuwal]', 'duj', '', ''),
(123, '[dutch#middle dutch]', 'dum', '', ''),
(124, '[dhivehi]', 'dv', '', ''),
(125, '[ewe]', 'ee', '', ''),
(126, '[egyptian]', 'egy', '', ''),
(127, '[greek]', 'el', '', ''),
(128, '[middle english]', 'enm', '', ''),
(129, '[esperanto]', 'eo', '', ''),
(130, '[eritai]', 'ert', '', ''),
(131, '[spanish]', 'es', '', ''),
(132, '[eshtehardi]', 'esh', '', ''),
(133, '[yup''ik]', 'esu', '', ''),
(134, '[estonian]', 'et', '', ''),
(135, '[basque]', 'eu', '', ''),
(136, '[even]', 'eve', '', ''),
(137, '[evenki]', 'evn', '', ''),
(138, '[persian]', 'fa', '', ''),
(139, '[fula]', 'ff', '', ''),
(140, '[finnish]', 'fi', '', ''),
(141, '[meänkieli]', 'fit', '', ''),
(142, '[fijian]', 'fj', '', ''),
(143, '[montana salish]', 'fla', '', ''),
(144, '[faroese]', 'fo', '', ''),
(145, '[fon]', 'fon', '', ''),
(146, '[siraya]', 'fos', '', ''),
(147, '[french]', 'fr', '', ''),
(148, '[french#middle french]', 'frm', '', ''),
(149, '[french#old french]', 'fro', '', ''),
(150, '[franco-provençal]', 'frp', '', ''),
(151, '[frisian#north frisian][north frisian]', 'frr', '', ''),
(152, '[friulian]', 'fur', '', ''),
(153, '[frisian#west frisian][west frisian]', 'fy', '', ''),
(154, '[irish]', 'ga', '', ''),
(155, '[gagauz]', 'gag', '', ''),
(156, '[gayo]', 'gay', '', ''),
(157, '[scottish gaelic]', 'gd', '', ''),
(158, '[godoberi]', 'gdo', '', ''),
(159, '[mehri]', 'gdq', '', ''),
(160, '[ge''ez]', 'gez', '', ''),
(161, '[gilbertese]', 'gil', '', ''),
(162, '[hinukh]', 'gin', '', ''),
(163, '[galician]', 'gl', '', ''),
(164, '[nanai]', 'gld', '', ''),
(165, '[german#middle high german]', 'gmh', '', ''),
(166, '[low german#middle low german]', 'gml', '', ''),
(167, '[guaraní]', 'gn', '', ''),
(168, '[gooniyandi]', 'gni', '', ''),
(169, '[german#old high german]', 'goh', '', ''),
(170, '[gothic]', 'got', '', ''),
(171, '[greek#ancient][greek#ancient greek]', 'grc', '', ''),
(172, '[alemannic german]', 'gsw', '', ''),
(173, '[gujarati]', 'gu', '', ''),
(174, '[manx]', 'gv', '', ''),
(175, '[hausa]', 'ha', '', ''),
(176, '[haida]', 'hai', '', ''),
(177, '[chinese#hakka]', 'hak', '', ''),
(178, '[hawaiian]', 'haw', '', ''),
(179, '[hebrew]', 'he', '', ''),
(180, '[hindi]', 'hi', '', ''),
(181, '[hiligaynon]', 'hil', '', ''),
(182, '[hittite]', 'hit', '', ''),
(183, '[hopi]', 'hop', '', ''),
(184, '[sorbian#upper sorbian]', 'hsb', '', ''),
(185, '[haitian creole]', 'ht', '', ''),
(186, '[hungarian]', 'hu', '', ''),
(187, '[hunzib]', 'huz', '', ''),
(188, '[armenian]', 'hy', '', ''),
(189, '[interlingua]', 'ia', '', ''),
(190, '[iban]', 'iba', '', ''),
(191, '[indonesian]', 'id', '', ''),
(192, '[interlingue]', 'ie', '', ''),
(193, '[igbo]', 'ig', '', ''),
(194, '[sichuan yi]', 'ii', '', ''),
(195, '[iranun]', 'ill', '', ''),
(196, '[ilocano]', 'ilo', '', ''),
(197, '[ingush]', 'inh', '', ''),
(198, '[ido]', 'io', '', ''),
(199, '[mingo]', 'iro-min', '', ''),
(200, '[icelandic]', 'is', '', ''),
(201, '[istriot]', 'ist', '', ''),
(202, '[italian]', 'it', '', ''),
(203, '[itelmen]', 'itl', '', ''),
(204, '[inuktitut]', 'iu', '', ''),
(205, '[ingrian]', 'izh', '', ''),
(206, '[japanese]', 'ja', '', ''),
(207, '[yan-nhangu]', 'jay', '', ''),
(208, '[lojban]', 'jbo', '', ''),
(209, '[yamdena]', 'jmd', '', ''),
(210, '[jarai]', 'jra', '', ''),
(211, '[javanese]', 'jv', '', ''),
(212, '[georgian]', 'ka', '', ''),
(213, '[karakalpak]', 'kaa', '', ''),
(214, '[kabyle]', 'kab', '', ''),
(215, '[bezhta]', 'kap', '', ''),
(216, '[javanese#old javanese]', 'kaw', '', ''),
(217, '[kabardian]', 'kbd', '', ''),
(218, '[khanty]', 'kca', '', ''),
(219, '[kemak]', 'kem', '', ''),
(220, '[ket]', 'ket', '', ''),
(221, '[kongo]', 'kg', '', ''),
(222, '[kikuyu]', 'ki', '', ''),
(223, '[kickapoo]', 'kic', '', ''),
(224, '[khakas]', 'kjh', '', ''),
(225, '[khinalug]', 'kjj', '', ''),
(226, '[kazakh]', 'kk', '', ''),
(227, '[guugu yimidhirr]', 'kky', '', ''),
(228, '[greenlandic]', 'kl', '', ''),
(229, '[klamath-modoc]', 'kla', '', ''),
(230, '[gamilaraay]', 'kld', '', ''),
(231, '[khmer]', 'km', '', ''),
(232, '[kurdish#kurmanji]', 'kmr', '', ''),
(233, '[kannada]', 'kn', '', ''),
(234, '[kankanaey]', 'kne', '', ''),
(235, '[korean]', 'ko', '', ''),
(236, '[komi-permyak]', 'koi', '', ''),
(237, '[konkani]', 'kok', '', ''),
(238, '[karata]', 'kpt', '', ''),
(239, '[komi-zyrian]', 'kpv', '', ''),
(240, '[koryak]', 'kpy', '', ''),
(241, '[kimaragang]', 'kqr', '', ''),
(242, '[karachay-balkar]', 'krc', '', ''),
(243, '[karelian]', 'krl', '', ''),
(244, '[kedang]', 'ksx', '', ''),
(245, '[kurdish#sorani][kurdish#kurmanji]', 'ku', '', ''),
(246, '[kumyk]', 'kum', '', ''),
(247, '[kutenai]', 'kut', '', ''),
(248, '[bagvalal]', 'kva', '', ''),
(249, '[cornish]', 'kw', '', ''),
(250, '[kyrgyz]', 'ky', '', ''),
(251, '[coastal kadazan]', 'kzj', '', ''),
(252, '[latin]', 'la', '', ''),
(253, '[ladino]', 'lad', '', ''),
(254, '[luxembourgish]', 'lb', '', ''),
(255, '[lak]', 'lbe', '', ''),
(256, '[lezgi]', 'lez', '', ''),
(257, '[limburgish]', 'li', '', ''),
(258, '[ligurian]', 'lij', '', ''),
(259, '[livonian]', 'liv', '', ''),
(260, '[lampung api]', 'ljp', '', ''),
(261, '[lakota]', 'lkt', '', ''),
(262, '[ladin]', 'lld', '', ''),
(263, '[lombard]', 'lmo', '', ''),
(264, '[lingala]', 'ln', '', ''),
(265, '[lao]', 'lo', '', ''),
(266, '[lithuanian]', 'lt', '', ''),
(267, '[latgalian]', 'ltg', '', ''),
(268, '[leti]', 'lti', '', ''),
(269, '[luhya]', 'luy', '', ''),
(270, '[latvian]', 'lv', '', ''),
(271, '[laz]', 'lzz', '', ''),
(272, '[madurese]', 'mad', '', ''),
(273, '[makasar]', 'mak', '', ''),
(274, '[maasai]', 'mas', '', ''),
(275, '[western bukidnon manobo]', 'mbb', '', ''),
(276, '[moksha]', 'mdf', '', ''),
(277, '[mandar]', 'mdr', '', ''),
(278, '[central melanau]', 'mel', '', ''),
(279, '[mende]', 'men', '', ''),
(280, '[meru]', 'mer', '', ''),
(281, '[mauritian creole]', 'mfe', '', ''),
(282, '[mayo]', 'mfy', '', ''),
(283, '[malagasy]', 'mg', '', ''),
(284, '[maori]', 'mi', '', ''),
(285, '[miami]', 'mia', '', ''),
(286, '[mi''kmaq]', 'mic', '', ''),
(287, '[minangkabau]', 'min', '', ''),
(288, '[southern puebla mixtec]', 'mit', '', ''),
(289, '[macedonian]', 'mk', '', ''),
(290, '[malayalam]', 'ml', '', ''),
(291, '[western maninkakan]', 'mlq', '', ''),
(292, '[mongolian]', 'mn', '', ''),
(293, '[manchu]', 'mnc', '', ''),
(294, '[mansi]', 'mns', '', ''),
(295, '[montagnais]', 'moe', '', ''),
(296, '[mohawk]', 'moh', '', ''),
(297, '[marathi]', 'mr', '', ''),
(298, '[maricopa]', 'mrc', '', ''),
(299, '[maranao]', 'mrw', '', ''),
(300, '[malay][malay#rumi][malay#jawi]', 'ms', '', ''),
(301, '[maltese]', 'mt', '', ''),
(302, '[creek]', 'mus', '', ''),
(303, '[miyako]', 'mvi', '', ''),
(304, '[tagal murut]', 'mvv', '', ''),
(305, '[burmese]', 'my', '', ''),
(306, '[pirahã]', 'myp', '', ''),
(307, '[erzya]', 'myv', '', ''),
(308, '[mazanderani]', 'mzn', '', ''),
(309, '[nauruan]', 'na', '', ''),
(310, '[nahuatl]', 'nah', '', ''),
(311, '[chinese#min nan]', 'nan', '', ''),
(312, '[neapolitan]', 'nap', '', ''),
(313, '[nama]', 'naq', '', ''),
(314, '[classical nahuatl]', 'nci', '', ''),
(315, '[low german]', 'nds', '', ''),
(316, '[low german#german low german]', 'nds-de', '', ''),
(317, '[nepali]', 'ne', '', ''),
(318, '[newari]', 'new', '', ''),
(319, '[nias]', 'nia', '', ''),
(320, '[nganasan]', 'nio', '', ''),
(321, '[nivkh]', 'niv', '', ''),
(322, '[dutch]', 'nl', '', ''),
(323, '[norwegian#nynorsk]', 'nn', '', ''),
(324, '[norwegian][norwegian#bokmål]', 'no', '', ''),
(325, '[nogai]', 'nog', '', ''),
(326, '[old norse]', 'non', '', ''),
(327, '[novial]', 'nov', '', ''),
(328, '[nuer]', 'nus', '', ''),
(329, '[navajo]', 'nv', '', ''),
(330, '[occitan]', 'oc', '', ''),
(331, '[frisian#old frisian]', 'ofs', '', ''),
(332, '[ojibwe]', 'oj', '', ''),
(333, '[o''odham]', 'ood', '', ''),
(334, '[oriya]', 'or', '', ''),
(335, '[old east slavic]', 'orv', '', ''),
(336, '[ossetian#digor][ossetian][ossetian#iron]', 'os', '', ''),
(337, '[osage]', 'osa', '', ''),
(338, '[old saxon]', 'osx', '', ''),
(339, '[ottoman turkish]', 'ota', '', ''),
(340, '[punjabi]', 'pa', '', ''),
(341, '[pangasinan]', 'pag', '', ''),
(342, '[persian#middle persian][middle persian]', 'pal', '', ''),
(343, '[kapampangan]', 'pam', '', ''),
(344, '[papiamentu]', 'pap', '', ''),
(345, '[plautdietsch]', 'pdt', '', ''),
(346, '[phoenician]', 'phn', '', ''),
(347, '[pali]', 'pi', '', ''),
(348, '[powhatan]', 'pim', '', ''),
(349, '[pitjantjatjara]', 'pjt', '', ''),
(350, '[polish]', 'pl', '', ''),
(351, '[paulohi]', 'plh', '', ''),
(352, '[pamona]', 'pmf', '', ''),
(353, '[malecite-passamaquoddy]', 'pqm', '', ''),
(354, '[old prussian]', 'prg', '', ''),
(355, '[pashto]', 'ps', '', ''),
(356, '[portuguese]', 'pt', '', ''),
(357, '[quechua]', 'qu', '', ''),
(358, '[quileute]', 'qui', '', ''),
(359, '[rade]', 'rad', '', ''),
(360, '[rapa nui]', 'rap', '', ''),
(361, '[romagnol]', 'rgn', '', ''),
(362, '[rohingya]', 'rhg', '', ''),
(363, '[romansch]', 'rm', '', ''),
(364, '[kirundi]', 'rn', '', ''),
(365, '[romanian]', 'ro', '', ''),
(366, '[guernésiais]', 'roa-grn', '', ''),
(367, '[jèrriais]', 'roa-jer', '', ''),
(368, '[leonese]', 'roa-leo', '', ''),
(369, '[norman]', 'roa-nor', '', ''),
(370, '[portuguese#old portuguese]', 'roa-opt', '', ''),
(371, '[tae'']', 'rob', '', ''),
(372, '[romani]', 'rom', '', ''),
(373, '[russian]', 'ru', '', ''),
(374, '[rusyn]', 'rue', '', ''),
(375, '[aromanian]', 'rup', '', ''),
(376, '[megleno-romanian]', 'ruq', '', ''),
(377, '[rutul]', 'rut', '', ''),
(378, '[kinyarwanda]', 'rw', '', ''),
(379, '[yaeyama]', 'rys', '', ''),
(380, '[okinawan]', 'ryu', '', ''),
(381, '[sanskrit]', 'sa', '', ''),
(382, '[yakut]', 'sah', '', ''),
(383, '[sasak]', 'sas', '', ''),
(384, '[santali]', 'sat', '', ''),
(385, '[sardinian]', 'sc', '', ''),
(386, '[sicilian]', 'scn', '', ''),
(387, '[scots]', 'sco', '', ''),
(388, '[sindhi]', 'sd', '', ''),
(389, '[sassarese sardinian]', 'sdc', '', ''),
(390, '[sardinian#gallurese sardinian]', 'sdn', '', ''),
(391, '[northern sami]', 'se', '', ''),
(392, '[seri]', 'sei', '', ''),
(393, '[irish#old irish]', 'sga', '', ''),
(394, '[serbo-croatian#cyrillic][serbo-croatian#roman][serbo-croatian]', 'sh', '', ''),
(395, '[shoshone]', 'shh', '', ''),
(396, '[tashelhit]', 'shi', '', ''),
(397, '[shan]', 'shn', '', ''),
(398, '[sinhalese]', 'si', '', ''),
(399, '[sebop]', 'sib', '', ''),
(400, '[kildin sami]', 'sjd', '', ''),
(401, '[ter sami]', 'sjt', '', ''),
(402, '[slovak]', 'sk', '', ''),
(403, '[slovene]', 'sl', '', ''),
(404, '[samoan]', 'sm', '', ''),
(405, '[inari sami]', 'smn', '', ''),
(406, '[simeulue]', 'smr', '', ''),
(407, '[skolt sami]', 'sms', '', ''),
(408, '[sumbawa]', 'smw', '', ''),
(409, '[shona]', 'sn', '', ''),
(410, '[bau bidayuh]', 'sne', '', ''),
(411, '[somali]', 'so', '', ''),
(412, '[albanian]', 'sq', '', ''),
(413, '[sardinian#campidanese sardinian]', 'sro', '', ''),
(414, '[swazi]', 'ss', '', ''),
(415, '[sotho]', 'st', '', ''),
(416, '[frisian#saterland frisian]', 'stq', '', ''),
(417, '[saanich]', 'str', '', ''),
(418, '[sundanese]', 'su', '', ''),
(419, '[sumerian]', 'sux', '', ''),
(420, '[swedish]', 'sv', '', ''),
(421, '[swahili]', 'sw', '', ''),
(422, '[sangir]', 'sxn', '', ''),
(423, '[aramaic#classical syriac]', 'syc', '', ''),
(424, '[silesian]', 'szl', '', ''),
(425, '[tamil]', 'ta', '', ''),
(426, '[tabasaran]', 'tab', '', ''),
(427, '[torres strait creole]', 'tcs', '', ''),
(428, '[tulu]', 'tcy', '', ''),
(429, '[telugu]', 'te', '', ''),
(430, '[tetum]', 'tet', '', ''),
(431, '[dena''ina]', 'tfn', '', ''),
(432, '[tajik]', 'tg', '', ''),
(433, '[thai]', 'th', '', ''),
(434, '[tigrinya]', 'ti', '', ''),
(435, '[tigre]', 'tig', '', ''),
(436, '[tindi]', 'tin', '', ''),
(437, '[turkmen]', 'tk', '', ''),
(438, '[tsakhur]', 'tkr', '', ''),
(439, '[tagalog]', 'tl', '', ''),
(440, '[tlingit]', 'tli', '', ''),
(441, '[talysh][talysh#cyrillic][talysh#latin]', 'tly', '', ''),
(442, '[tswana]', 'tn', '', ''),
(443, '[tontemboan]', 'tnt', '', ''),
(444, '[tongan]', 'to', '', ''),
(445, '[tok pisin]', 'tpi', '', ''),
(446, '[tupinambá]', 'tpn', '', ''),
(447, '[tonkawa]', 'tqw', '', ''),
(448, '[turkish]', 'tr', '', ''),
(449, '[tatar]', 'tt', '', ''),
(450, '[tat]', 'ttt', '', ''),
(451, '[twi]', 'tw', '', ''),
(452, '[taos]', 'twf', '', ''),
(453, '[tocharian b]', 'txb', '', ''),
(454, '[tahitian]', 'ty', '', ''),
(455, '[tuvan]', 'tyv', '', ''),
(456, '[tz''utujil]', 'tzj', '', ''),
(457, '[central atlas tamazight]', 'tzm', '', ''),
(458, '[udi]', 'udi', '', ''),
(459, '[udmurt]', 'udm', '', ''),
(460, '[uyghur]', 'ug', '', ''),
(461, '[ugaritic]', 'uga', '', ''),
(462, '[ukrainian]', 'uk', '', ''),
(463, '[umbundu]', 'umb', '', ''),
(464, '[lenape#munsee]', 'umu', '', ''),
(465, '[unami][lenape#unami]', 'unm', '', ''),
(466, '[urdu]', 'ur', '', ''),
(467, '[uzbek][uzbek#cyrillic]', 'uz', '', ''),
(468, '[venda]', 've', '', ''),
(469, '[venetian]', 'vec', '', ''),
(470, '[veps]', 'vep', '', ''),
(471, '[vietnamese]', 'vi', '', ''),
(472, '[mbabaram]', 'vmb', '', ''),
(473, '[volapük]', 'vo', '', ''),
(474, '[votic]', 'vot', '', ''),
(475, '[võro]', 'vro', '', ''),
(476, '[walloon]', 'wa', '', ''),
(477, '[waray-waray]', 'war', '', ''),
(478, '[warlpiri]', 'wbp', '', ''),
(479, '[wik-mungkan]', 'wim', '', ''),
(480, '[welsh#middle welsh]', 'wlm', '', ''),
(481, '[wallisian]', 'wls', '', ''),
(482, '[wolof]', 'wo', '', ''),
(483, '[chinese#wu]', 'wuu', '', ''),
(484, '[wangaaybuwan-ngiyambaa]', 'wyb', '', ''),
(485, '[vilamovian]', 'wym', '', ''),
(486, '[kalmyk]', 'xal', '', ''),
(487, '[breton#middle breton]', 'xbm', '', ''),
(488, '[armenian#old armenian]', 'xcl', '', ''),
(489, '[darkinjung]', 'xda', '', ''),
(490, '[xhosa]', 'xh', '', ''),
(491, '[hadrami]', 'xhd', '', ''),
(492, '[bakung]', 'xkl', '', ''),
(493, '[median]', 'xme', '', ''),
(494, '[mingrelian]', 'xmf', '', ''),
(495, '[phrygian]', 'xpg', '', ''),
(496, '[sabaean]', 'xsa', '', ''),
(497, '[sherpa]', 'xsr', '', ''),
(498, '[tocharian a]', 'xto', '', ''),
(499, '[yiddish]', 'yi', '', ''),
(500, '[yidiny]', 'yii', '', ''),
(501, '[yindjibarndi]', 'yij', '', ''),
(502, '[yakan]', 'yka', '', ''),
(503, '[northern yukaghir]', 'ykg', '', ''),
(504, '[yoruba]', 'yo', '', ''),
(505, '[yonaguni]', 'yoi', '', ''),
(506, '[nenets]', 'yrk', '', ''),
(507, '[yucatec maya]', 'yua', '', ''),
(508, '[yuchi]', 'yuc', '', ''),
(509, '[chinese#cantonese]', 'yue', '', ''),
(510, '[havasupai-walapai-yavapai]', 'yuf', '', ''),
(511, '[isthmus zapotec]', 'zai', '', ''),
(512, '[central berawan]', 'zbc', '', ''),
(513, '[ngazidja comorian]', 'zdj', '', ''),
(514, '[záparo]', 'zro', '', ''),
(515, '[zulu]', 'zu', '', ''),
(516, '[zazaki]', 'zza', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `user_info`
--

CREATE TABLE `user_info` (
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_location` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_performances`
--

CREATE TABLE `user_performances` (
  `user_id` bigint(20) NOT NULL,
  `word_connection_id` bigint(20) NOT NULL,
  `learning_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `performance` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_languages`
--

CREATE TABLE `user_languages` (
  `user_id` bigint(20) NOT NULL,
  `base_language` int(11) NOT NULL,
  `learning_language` int(11) NOT NULL,
  `page` int(11) NOT NULL,
  `meta_info` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `under_use` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `words`
--

CREATE TABLE `words` (
`word_id` bigint(20) NOT NULL,
  `language_id` int(11) NOT NULL,
  `word` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `meta_info` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `contributor_id` char(4) COLLATE utf8_unicode_ci NOT NULL,
  `translation_md5` binary(16) NOT NULL COMMENT 'the sig for global translation or clarification'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `word_connections`
--

CREATE TABLE `word_connections` (
`connection_id` bigint(20) NOT NULL,
  `word1_id` bigint(20) NOT NULL,
  `word1_language` int(11) NOT NULL,
  `word2_id` bigint(20) NOT NULL,
  `word2_language` int(11) NOT NULL,
  `connection_type` smallint(6) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `contributor_id` char(4) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `languages`
--
ALTER TABLE `languages`
 ADD PRIMARY KEY (`language_id`), ADD UNIQUE KEY `code` (`code`);

--
-- Indexes for table `user_info`
--
ALTER TABLE `user_info`
ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `user_languages`
--
ALTER TABLE `user_languages`
ADD PRIMARY KEY (`user_id`,`base_language`,`learning_language`);

--
-- Indexes for table `user_performances`
--
ALTER TABLE `user_performances`
ADD PRIMARY KEY (`user_id`,`word_connection_id`,`learning_type`);

--
-- Indexes for table `words`
--
ALTER TABLE `words`
 ADD PRIMARY KEY (`word_id`), ADD UNIQUE KEY `unique_translation` (`language_id`,`word`,`translation_md5`), ADD KEY `word` (`word`), ADD KEY `language_id` (`language_id`), ADD KEY `translation_md5` (`translation_md5`);

--
-- Indexes for table `word_connections`
--
ALTER TABLE `word_connections`
 ADD PRIMARY KEY (`connection_id`), ADD UNIQUE KEY `unique_connection` (`word1_id`,`word2_id`,`connection_type`,`contributor_id`), ADD KEY `word1_id` (`word1_id`), ADD KEY `word1_language` (`word1_language`), ADD KEY `word2_id` (`word2_id`), ADD KEY `word2_language` (`word2_language`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `languages`
--
ALTER TABLE `languages`
MODIFY `language_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user_info`
--
ALTER TABLE `user_info`
MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `words`
--
ALTER TABLE `words`
MODIFY `word_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `word_connections`
--
ALTER TABLE `word_connections`
MODIFY `connection_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `word_connections`
--
ALTER TABLE `word_connections`
ADD CONSTRAINT `word2_lang_fk` FOREIGN KEY (`word2_language`) REFERENCES `languages` (`language_id`),
ADD CONSTRAINT `word1_fk` FOREIGN KEY (`word1_id`) REFERENCES `words` (`word_id`),
ADD CONSTRAINT `word1_lang_fk` FOREIGN KEY (`word1_language`) REFERENCES `languages` (`language_id`),
ADD CONSTRAINT `word2_fk` FOREIGN KEY (`word2_id`) REFERENCES `words` (`word_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
