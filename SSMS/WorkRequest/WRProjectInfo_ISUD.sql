SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO
alter  PROCEDURE uspWRProjectInfo_ISUD
		(
	@Background VARCHAR(max)='',
	@BranchId INT=1,
	@ContinentID INT=1,
	@ContractNumber VARCHAR(255)='',
	@DirWR INT=1,
	@FundSourceID INT=1,
	@FundSourceText VARCHAR(255)='fun',
	@InstallationID INT=1,
	@LocationID INT=1,
	@MilitarySiteID INT=1,
	@OrgCode VARCHAR(255)='org',
	@ProgramYear VARCHAR(255)='2021',
	@ProjectName VARCHAR(255)='name',
	@ProjectNotes Varchar(max)='notes',
	@ProjectNumber VARCHAR(255)='1',
	@PulseCityRegionID INT=2,
	@PulseCountryID INT =2,
	@SectionId INT =1,
	@Site VARCHAR(255)='site',
	@WRID INT=2,
	@TYPE_ACTION VARCHAR(25)='select'
	)
 
AS
 

-- =============================================
-- Author:		MUCHACHOLAPTOP\dave_
-- Create date: 2021/11/24
-- Description:	Shreds an XML document to relational table intended to Create, Update, or Delete rows in 'WRProjectInfo'
-- After running do the following: 
-- INSERT fixes:
-- 1. Replace TABLE with the ID of the target table in entire procedure
-- 2. Remove comma after the last column in the CREATE Table statement
-- 3. Remove <sql_system_user> from last_update_date BY_WHO as appropriate from test data/ SELECT in INSERT. 
-- 4. Remove all computed columns from the CREATE/INSERT/SELECT statements and test XML
-- 5. Remove the TABLE_ID Column from the CREATE/INSERT/SELECT statements
-- 6. Add TYPE_ACTION to SP Variables, add a BEGIN after the AS in the ISUD Proc
-- 7. Set correct dates/non nulls in test data. Remove all other bugs I wasn't smart enough TO find
-- UPDATE fixes:
-- 1. Replace TABLE_ID with ID of target table
-- 2. Remove comma after the last column in the SET column list.
-- 3. PROD.LAST_UDPATE_DATE = GETDATE(), SQL_SYSTEM_USER = SYSTEM_USER, BY_WHO= @BY_WHO
-- 4. Remove the update to the TABLE_ID
-- DELETE fixes:
-- =============================================
--START CODE HERE--

 
 
IF @TYPE_ACTION='UPDATE'
BEGIN
	UPDATE WRProjectInfo
	SET 
		Background = @Background,
		BranchId = @BranchId,
		ContinentID = @ContinentID,
		ContractNumber = @ContractNumber,
		DirWR = @DirWR,
		FundSourceID = @FundSourceID,
		FundSourceText = @FundSourceText,
		InstallationID = @InstallationID,
		LocationID = @LocationID,
		MilitarySiteID = @MilitarySiteID,
		OrgCode = @OrgCode,
		ProgramYear = @ProgramYear,
		ProjectName = @ProjectName,
		ProjectNotes = @ProjectNotes,
		ProjectNumber = @ProjectNumber,
		PulseCityRegionID = @PulseCityRegionID,
		PulseCountryID = @PulseCountryID,
		SectionId = @SectionId,
		Site = @Site
		--WRID = @WRID
		where WRID = @WRID
END
 
 
IF @TYPE_ACTION='INSERT'
BEGIN
	INSERT INTO WRProjectInfo
	(
	Background,
	BranchId,
	ContinentID,
	ContractNumber,
	DirWR,
	FundSourceID,
	FundSourceText,
	InstallationID,
	LocationID,
	MilitarySiteID,
	OrgCode,
	ProgramYear,
	ProjectName,
	ProjectNotes,
	ProjectNumber,
	PulseCityRegionID,
	PulseCountryID,
	SectionId,
	Site,
	WRID
	)
VALUES
(
	@Background,
	@BranchId,
	@ContinentID,
	@ContractNumber,
	@DirWR,
	@FundSourceID,
	@FundSourceText,
	@InstallationID,
	@LocationID,
	@MilitarySiteID,
	@OrgCode,
	@ProgramYear,
	@ProjectName,
	@ProjectNotes,
	@ProjectNumber,
	@PulseCityRegionID,
	@PulseCountryID,
	@SectionId,
	@Site,
	@WRID
)
END
--END CODE HERE--
 IF @TYPE_ACTION='SELECT'
 BEGIN
	select * from WRProjectInfo
	where WRID = @WRID
END

--CREATE  PROCEDURE uspWRProjectInfo_S
--	(@WRUser_ID(VARCHAR(1))
 
--AS

--	/*	
--	<GENERAL_INFO>
--		<AUTHOR>
--		MUCHACHOLAPTOP\dave_
--		</AUTHOR>
--		<DATECREATED>
--		Nov 24 2021  1:28PM
--		</DATECREATED>
--		<FILENAME>
--		uspWRProjectInfo_S
--		</FILENAME>
--		<APPNAME>
--		WorkRequest	
--		</APPNAME>
--		<DB>
--			DATABASE NAME
--		</DB>
--		<CALLEDBY NAME="example.asp" TYPE="ASP" />
--		<CALLS NAME="SQL VIEW EXAMPLE" TYPE="VIEW" />
--		<DESC>
--			PROVIDES METHOD FOR GETTING ENTITY DATA 
--		</DESC>		
--		<!--LIST OF CHANGES SUBMITTED-->
--		<CHANGE BY="" DATE="">
--			<DESC></DESC>
--		</CHANGE>
--	</GENERAL_INFO>
--	*/
	
----SELECT COLUMN DATA FOR SPROC READER
 
--IF (SELECT COUNT(*) FROM WRProjectInfo)>0
--BEGIN
--SELECT * FROM WRProjectInfo
--	ISNULL([WRProjectInfo].[Background],'')AS Background,
--	ISNULL([WRProjectInfo].[BranchId],0)AS BranchId,
--	ISNULL([WRProjectInfo].[ContinentID],0)AS ContinentID,
--	ISNULL([WRProjectInfo].[ContractNumber],'')AS ContractNumber,
--	ISNULL([WRProjectInfo].[DirWR],0)AS DirWR,
--	ISNULL([WRProjectInfo].[FundSourceID],0)AS FundSourceID,
--	ISNULL([WRProjectInfo].[FundSourceText],'')AS FundSourceText,
--	ISNULL([WRProjectInfo].[InstallationID],0)AS InstallationID,
--	ISNULL([WRProjectInfo].[LocationID],0)AS LocationID,
--	ISNULL([WRProjectInfo].[MilitarySiteID],0)AS MilitarySiteID,
--	ISNULL([WRProjectInfo].[OrgCode],'')AS OrgCode,
--	ISNULL([WRProjectInfo].[ProgramYear],'')AS ProgramYear,
--	ISNULL([WRProjectInfo].[ProjectName],'')AS ProjectName,
--	ISNULL([WRProjectInfo].[ProjectNotes],'')AS ProjectNotes,
--	ISNULL([WRProjectInfo].[ProjectNumber],'')AS ProjectNumber,
--	ISNULL([WRProjectInfo].[PulseCityRegionID],0)AS PulseCityRegionID,
--	ISNULL([WRProjectInfo].[PulseCountryID],0)AS PulseCountryID,
--	ISNULL([WRProjectInfo].[SectionId],0)AS SectionId,
--	ISNULL([WRProjectInfo].[Site],'')AS Site,
--	ISNULL([WRProjectInfo].[WRID],0)AS WRID,
--WHERE [IDENTITY COLUMN] = @WRUser_ID
--END
--ELSE
--BEGIN
 
----USE TO RETURN A ZERO DATA SET IF THE ROW NOT IN SELECTED TABLE - SOMETIMES NEEDED FOR CLASS
 
--	SELECT
--	''  AS Background,
--	0  AS BranchId,
--	0  AS ContinentID,
--	''  AS ContractNumber,
--	0  AS DirWR,
--	0  AS FundSourceID,
--	''  AS FundSourceText,
--	0  AS InstallationID,
--	0  AS LocationID,
--	0  AS MilitarySiteID,
--	''  AS OrgCode,
--	''  AS ProgramYear,
--	''  AS ProjectName,
--	''  AS ProjectNotes,
--	''  AS ProjectNumber,
--	0  AS PulseCityRegionID,
--	0  AS PulseCountryID,
--	0  AS SectionId,
--	''  AS Site,
--	0  AS WRID,
--END