<?php
	$doc = new DOMDocument();
	$doc->load('environment.xml');
	
	//echo "\<environment\>";
	
	$warehouses = $doc->getElementsByTagName("warehouse");
	foreach($warehouses as $warehouse){
		$name = $warehouse->getElementsByTagName("name")->item(0)->nodeValue;
		$filename = $warehouse->getElementsByTagName("fileName")->item(0)->nodeValue;
		$foldername = $warehouse->getElementsByTagName("folderName")->item(0)->nodeValue;
		$format = $warehouse->getElementsByTagName("format")->item(0)->nodeValue;
		$translationX = $warehouse->getElementsByTagName("translationX")->item(0)->nodeValue;
		$translationY = $warehouse->getElementsByTagName("translationY")->item(0)->nodeValue;
		$translationZ = $warehouse->getElementsByTagName("translationZ")->item(0)->nodeValue;
		$scale = $warehouse->getElementsByTagName("scale")->item(0)->nodeValue;
		$rotationX = $warehouse->getElementsByTagName("rotationX")->item(0)->nodeValue;
		$rotationY = $warehouse->getElementsByTagName("rotationY")->item(0)->nodeValue;
		$rotationZ = $warehouse->getElementsByTagName("rotationZ")->item(0)->nodeValue;
		$rotationW = $warehouse->getElementsByTagName("rotationW")->item(0)->nodeValue;
		/*echo "\<warehouse\>";
		echo "\<name\>".$name."\<\/name\>";
		echo "\<\/warehouse\>";*/
		$query = "INSERT INTO MODEL (typeid, name, fileName, folderName, format, translationX, translationY, translationZ, scale, rotationX, rotationY, rotationZ, rotationW) VALUES (";
		$query .= $name.",";
		$query .= $fileName.",";
		$query .= $folderName.",";
		$query .= $format.",";
		$query .= $translationX.",";
		$query .= $translationY.",";
		$query .= $translationZ.",";
		$query .= $scale.",";
		$query .= $rotationX.",";
		$query .= $rotationY.",";
		$query .= $rotationZ.",";
		$query .= $rotationW.");";
		echo $query;
	}
	
	$objects = $doc->getElementsByTagName("object");
	foreach($objects as $object){
		$name = $object->getElementsByTagName("name")->item(0)->nodeValue;
		$filename = $object->getElementsByTagName("fileName")->item(0)->nodeValue;
		$foldername = $object->getElementsByTagName("folderName")->item(0)->nodeValue;
		$format = $object->getElementsByTagName("format")->item(0)->nodeValue;
		$translationX = $object->getElementsByTagName("translationX")->item(0)->nodeValue;
		$translationY = $object->getElementsByTagName("translationY")->item(0)->nodeValue;
		$translationZ = $object->getElementsByTagName("translationZ")->item(0)->nodeValue;
		$scale = $object->getElementsByTagName("scale")->item(0)->nodeValue;
		$rotationX = $object->getElementsByTagName("rotationX")->item(0)->nodeValue;
		$rotationY = $object->getElementsByTagName("rotationY")->item(0)->nodeValue;
		$rotationZ = $object->getElementsByTagName("rotationZ")->item(0)->nodeValue;
		$rotationW = $object->getElementsByTagName("rotationW")->item(0)->nodeValue;
	}
	
	$racks = $doc->getElementsByTagName("rack");
	foreach($racks as $rack){
	}
	
	//echo "\<\/environment\>";
?>