<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["words"])) {
    $words_json = $_POST["words"];
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$ids = json_decode($words_json);
$audio_path = "audio/";
$database = new medoo();

$data = $database->select("word_audiocall", "url", ["AND" => ["word_id" => $ids, "type" => 0]]);
$path = array();

foreach ($data as $url) {
    array_push($path, $audio_path . $url);
}

$zipname = "temp_audio_package.zip";
$zip = new ZipArchive;
$zip->open($zipname, ZipArchive::CREATE);
foreach ($path as $file) {
    $zip->addFile($file);
}
$zip->close();

header("Content-Type: application/zip");
header("Content-disposition: attachment; filename=temp_audio_package.zip");
header("Content-Length: " . filesize($zipname));
readfile($zipname);

if (file_exists($zipname)) {
    unlink($zipname);
}