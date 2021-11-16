USE [Pulse];
GO
/****** Object:  StoredProcedure [dbo].[uspWRUser_ISUD]    Script Date: 11/16/2021 2:49:10 PM ******/
SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO
ALTER PROCEDURE uspWRUser_R
(@Emp_ID_NO VARCHAR(9) = 1)
AS

/*	
	<GENERAL_INFO>
		<AUTHOR>
		MUCHACHOLAPTOP\dave_
		</AUTHOR>
		<DATECREATED>
		Nov 16 2021  2:39PM
		</DATECREATED>
		<FILENAME>
		uspWRUser_R
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

BEGIN

    SELECT *
    FROM WRUser
    WHERE Emp_ID_NO = @Emp_ID_NO;
--IF (SELECT COUNT(*) FROM WRUser)>0
--BEGIN
--SELECT 
--	ISNULL([WRUser].[Country_Code],'')AS Country_Code,
--	ISNULL([WRUser].[Department],'')AS Department,
--	ISNULL([WRUser].[Display_Name],'')AS Display_Name,
--	ISNULL([WRUser].[DisplayName_Official],'')AS DisplayName_Official,
--	ISNULL([WRUser].[Email],'')AS Email,
--	ISNULL([WRUser].[Emp_ID_NO],'')AS Emp_ID_NO,
--	ISNULL([WRUser].[Emp_Type],'')AS Emp_Type,
--	ISNULL([WRUser].[First_Name],'')AS First_Name,
--	ISNULL([WRUser].[FLSA],'')AS FLSA,
--	ISNULL([WRUser].[Last_Name],'')AS Last_Name,
--	ISNULL([WRUser].[LoginDomain],'')AS LoginDomain,
--	ISNULL([WRUser].[LoginName],'')AS LoginName,
--	ISNULL([WRUser].[Office_Symbol],'')AS Office_Symbol,
--	ISNULL([WRUser].[Org_Code],'')AS Org_Code,
--	ISNULL([WRUser].[Phone],'')AS Phone,
--	ISNULL([WRUser].[Position_Title],'')AS Position_Title,
--	ISNULL([WRUser].[SharePointGroupId],0)AS SharePointGroupId
--	WHERE Emp_ID_NO = @Emp_ID_NO
--FROM [dbo].[WRUser]
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
--	''  AS FLSA,
--	''  AS Last_Name,
--	''  AS LoginDomain,
--	''  AS LoginName,
--	''  AS Office_Symbol,
--	''  AS Org_Code,
--	''  AS Phone,
--	''  AS Position_Title,
--	0  AS SharePointGroupId
--END
END;