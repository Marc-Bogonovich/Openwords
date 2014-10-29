<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["words"]) && isset($_POST["type"])) {
    $words_json = $_POST["words"];
    $type = $_POST["type"];
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$ids = json_decode($words_json);

$database = new medoo();

$data = $database->select("word_meaning", ["word_id(wordId)", "meaning_text(meaning)"], ["AND" => ["word_id" => $ids, "type" => $type]]);

header("Content-Type: application/json");
echo json_encode($data);
