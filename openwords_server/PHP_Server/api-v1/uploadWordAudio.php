<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["word"])) {
    $word_id = $_POST["word"];
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

if ($_FILES["file"]["error"] > 0) {
    echo "Error: " . $_FILES["file"]["error"] . "<br>";
} else {
    $new_name = $word_id . "_google_tts.mp3";
    move_uploaded_file($_FILES["file"]["tmp_name"], "audio/" . $new_name);

    $database = new medoo();
    $database->insert("word_audiocall", [
        "word_id" => $word_id,
        "url" => $new_name
    ]);

    echo "word " . $word_id . " ok";
}