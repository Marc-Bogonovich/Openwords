<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["lang1_id"]) && isset($_POST["page_number"]) && isset($_POST["lang2_id"]) && isset($_POST["rank_type"])) {
    $lang1_id = $_POST["lang1_id"];
    $lang2_id = $_POST["lang2_id"];
    $page_number = $_POST["page_number"];
    $rank_type = $_POST["rank_type"];

    if (isset($_POST["page_size"])) {
        $page_size = $_POST["page_size"];
        if ($page_size < 10 || $page_size > 100) {
            header("HTTP/1.1 400"); //For PHP 5.3
            echo "Page Size is too small or too large";
            return;
        }
    } else {
        $page_size = 20;
    }
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$database = new medoo();

$start_index = ($page_number - 1) * $page_size;

$sql = "SELECT DISTINCT langOneWordId, langTwoWordId FROM
(
SELECT words_lang1.id AS langOneWordId, ranked_words_lang2.id AS langTwoWordId, word_connections.id AS connectionId FROM
(SELECT id FROM words WHERE language_id=@lang1_id@) words_lang1,
word_connections,
(SELECT DISTINCT words.id FROM words, word_rank WHERE words.language_id=@lang2_id@ AND words.id=word_rank.word_id AND word_rank.rank_type=@rank@ ORDER BY word_rank.rank DESC LIMIT @start@,@size@)
ranked_words_lang2
WHERE
ranked_words_lang2.id=word_connections.word2_id AND words_lang1.id=word_connections.word1_id
) connections";

$sql2 = str_replace(array("@lang1_id@", "@lang2_id@", "@start@", "@size@", "@rank@"), array($lang1_id, $lang2_id, $start_index, $page_size, $rank_type), $sql);

$connections = $database->query($sql2)->fetchAll();

header("Content-Type: application/json");
echo json_encode(array(
    "connections" => $connections));

