USE [Pulse]
GO
/****** Object:  StoredProcedure [dbo].[uspWRUser_S]    Script Date: 11/22/2021 8:27:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER  PROCEDURE [dbo].[uspWRUser_S]
	(@WRUser_ID INT)
 
AS

	/*	
	<GENERAL_INFO>
		<AUTHOR>
		MUCHACHOLAPTOP\dave_
		</AUTHOR>
		<DATECREATED>
		Nov 22 2021  5:09PM
		</DATECREATED>
		<FILENAME>
		uspWRUser_S
		</FILENAME>
		<APPNAME>
		WorkRequest	
		</APPNAME>
		<DB>
			DATABASE NAME
		</DB>
		<CALLEDBY NAME="example.asp" TYPE="ASP" />
		<CALLS NAME="SQL VIEW EXAMPLE" TYPE="VIEW" />
		<DESC>
			PROVIDES METHOD FOR GETTING ENTITY DATA 
		</DESC>		
		<!--LIST OF CHANGES SUBMITTED-->
		<CHANGE BY="" DATE="">
			<DESC></DESC>
		</CHANGE>
	</GENERAL_INFO>
	*/
	
--SELECT COLUMN DATA FOR SPROC READER
BEGIN
 Select * from [dbo].[WRUser]
 where WRUser_ID = @WRUser_ID 
--IF (SELECT COUNT(*) FROM WRUser)>0
--BEGIN
--SELECT * FROM WRUser
--	ISNULL([WRUser].[Country_Code],'')AS Country_Code,
--	ISNULL([WRUser].[Department],'')AS Department,
--	ISNULL([WRUser].[Display_Name],'')AS Display_Name,
--	ISNULL([WRUser].[DisplayName_Official],'')AS DisplayName_Official,
--	ISNULL([WRUser].[Email],'')AS Email,
--	ISNULL([WRUser].[Emp_ID_NO],'')AS Emp_ID_NO,
--	ISNULL([WRUser].[Emp_Type],'')AS Emp_Type,
--	ISNULL([WRUser].[First_Name],'')AS First_Name,
--	ISNULL([WRUser].[Last_Name],'')AS Last_Name,
--	ISNULL([WRUser].[LoginDomain],'')AS LoginDomain,
--	ISNULL([WRUser].[LoginName],'')AS LoginName,
--	ISNULL([WRUser].[Office_Symbol],'')AS Office_Symbol,
--	ISNULL([WRUser].[Org_Code],'')AS Org_Code,
--	ISNULL([WRUser].[Phone],'')AS Phone,
--	ISNULL([WRUser].[Position_Title],'')AS Position_Title,
--	ISNULL([WRUser].[WRUser_ID],0)AS WRUser_ID,
--WHERE [IDENTITY COLUMN] = @WRUser_ID
--END
--ELSE
--BEGIN
 
----USE TO RETURN A ZERO DATA SET IF THE ROW NOT IN SELECTED TABLE - SOMETIMES NEEDED FOR CLASS
 
--	SELECT
--	''  AS Country_Code,
--	''  AS Department,
--	''  AS Display_Name,
--	''  AS DisplayName_Official,
--	''  AS Email,
--	''  AS Emp_ID_NO,
--	''  AS Emp_Type,
--	''  AS First_Name,
--	''  AS Last_Name,
--	''  AS LoginDomain,
--	''  AS LoginName,
--	''  AS Office_Symbol,
--	''  AS Org_Code,
--	''  AS Phone,
--	''  AS Position_Title,
--	0  AS WRUser_ID,
END