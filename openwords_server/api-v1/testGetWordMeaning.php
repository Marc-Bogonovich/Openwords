<?php

require_once "db.words.medoo.min.php";

$database = new medoo();

$data = $database->query("SELECT * FROM words, word_meaning WHERE language_id=11 and words.id=word_meaning.word_id limit 20")->fetchAll();


header("Content-Type: application/json");
echo json_encode(array("meanings" => $data));


