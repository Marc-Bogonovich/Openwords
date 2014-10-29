<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["lang_id"]) && isset($_POST["page_number"])) {
    $lang_id = $_POST["lang_id"];
    $page_number = $_POST["page_number"];

    if (isset($_POST["page_size"])) {
        $page_size = $_POST["page_size"];
        if ($page_size < 10) {
            header("HTTP/1.1 400"); //For PHP 5.3
            echo "Page Size is too small";
            return;
        }
    } else {
        $page_size = 100;
    }
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$database = new medoo();

$start_index = ($page_number - 1) * $page_size;

$sql = "select * from words where language_id=" . $lang_id
        . " limit " . $start_index . "," . $page_size;

$words = $database->query($sql)->fetchAll();

header("Content-Type: application/json");
echo json_encode(array("word_selection_id" => $word_selection_id,
    "page_number" => $page_number,
    "words_size" => count($words),
    "words" => $words));

