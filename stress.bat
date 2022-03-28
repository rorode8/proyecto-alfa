@echo off
cd "C:\Users\Yosshua\Desktop\ITAM\OCTAVO\DISTRIBUIDOS\proyecto-alfa"

FOR %%I IN (50,100,150,200,250,300,350,400,450,500) DO (
	echo Rep %%I
	
	FOR %%A IN (1 2 3 4 5 6 7 8 9 10) DO (
		java -jar Deployer.jar %%I >> "C:\Distribuidos\%%I.txt"		
	)
)
cd "C:\Distribuidos"