

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO
alter  PROCEDURE uspWRWorkRequest_ISUD
		(
	@AverageRate DECIMAL =1.1,
	@BranchAssignmentID INT =2,
	@BranchID INT =2 ,
	@CompletionDate DATETIME2='',
	@DatePrepared DATETIME2='',
	@DraftDueDate DATETIME='',
	@FinancialInfoID INT =2,
	@FY INT =2021,
	@ID INT = 2,
	@ProjectInfoID INT = 2,
	@ProjectManager VARCHAR(255)='PM',
	@ProjectPulseID INT =2,
	@Requestor VARCHAR(255) ='me',
	@StartDate DATETIME2='',
	@SubmissionDate DATETIME2='',
	@Supervisor VARCHAR(255)='superivsor',
	@WRNumber VARCHAR(255)='wrnum',
	@WRStatusID INT=2,
	@TYPE_ACTION VARCHAR(25)='select '
 
	)
 
AS
 

-- =============================================
-- Author:		MUCHACHOLAPTOP\dave_
-- Create date: 2021/11/24
-- Description:	Shreds an XML document to relational table intended to Create, Update, or Delete rows in 'WRWorkRequest'
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
	UPDATE WRWorkRequest
	SET 
		AverageRate = @AverageRate,
		BranchAssignmentID = @BranchAssignmentID,
		BranchID = @BranchID,
		CompletionDate = @CompletionDate,
		DatePrepared = @DatePrepared,
		DraftDueDate = @DraftDueDate,
		FinancialInfoID = @FinancialInfoID,
		FY = @FY,
		--ID = @ID,
		ProjectInfoID = @ProjectInfoID,
		ProjectManager = @ProjectManager,
		ProjectPulseID = @ProjectPulseID,
		Requestor = @Requestor,
		StartDate = @StartDate,
		SubmissionDate = @SubmissionDate,
		Supervisor = @Supervisor,
		WRNumber = @WRNumber,
		WRStatusID = @WRStatusID
		Where ID = @ID
END
 
 
IF @TYPE_ACTION='INSERT'
BEGIN
	INSERT INTO WRWorkRequest
	(
	AverageRate,
	BranchAssignmentID,
	BranchID,
	CompletionDate,
	DatePrepared,
	DraftDueDate,
	FinancialInfoID,
	FY,
	--ID,
	ProjectInfoID,
	ProjectManager,
	ProjectPulseID,
	Requestor,
	StartDate,
	SubmissionDate,
	Supervisor,
	WRNumber,
	WRStatusID
	)
VALUES
(
	@AverageRate,
	@BranchAssignmentID,
	@BranchID,
	@CompletionDate,
	@DatePrepared,
	@DraftDueDate,
	@FinancialInfoID,
	@FY,
	--@ID,
	@ProjectInfoID,
	@ProjectManager,
	@ProjectPulseID,
	@Requestor,
	@StartDate,
	@SubmissionDate,
	@Supervisor,
	@WRNumber,
	@WRStatusID
)
END

IF @TYPE_ACTION='SELECT'
BEGIN
	select * from [dbo].[WRWorkRequest]
	where ID = @ID
END
 
 
--GRANT  EXECUTE  ON [dbo].[EAS_GET_POC]  TO [WEB_APPUSER]
--GO
 
--SET QUOTED_IDENTIFIER OFF
--GO
--SET ANSI_NULLS OFF
--GO
 
 
 
--SET QUOTED_IDENTIFIER ON 
--GO
--SET ANSI_NULLS ON 
--GO
--CREATE  PROCEDURE uspWRWorkRequest_S
--	(@ID(VARCHAR(1))
 
--AS

--	/*	
--	<GENERAL_INFO>
--		<AUTHOR>
--		MUCHACHOLAPTOP\dave_
--		</AUTHOR>
--		<DATECREATED>
--		Nov 24 2021  1:51PM
--		</DATECREATED>
--		<FILENAME>
--		uspWRWorkRequest_S
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
 
--IF (SELECT COUNT(*) FROM WRWorkRequest)>0
--BEGIN
--SELECT * FROM WRWorkRequest
----AverageRate | decimal
--	ISNULL([WRWorkRequest].[BranchAssignmentID],0)AS BranchAssignmentID,
--	ISNULL([WRWorkRequest].[BranchID],0)AS BranchID,
----CompletionDate | datetime2
----DatePrepared | datetime2
--	ISNULL([WRWorkRequest].[DraftDueDate],'01-JAN-1900')AS DraftDueDate,
--	ISNULL([WRWorkRequest].[FinancialInfoID],0)AS FinancialInfoID,
--	ISNULL([WRWorkRequest].[FY],0)AS FY,
--	ISNULL([WRWorkRequest].[ID],0)AS ID,
--	ISNULL([WRWorkRequest].[ProjectInfoID],0)AS ProjectInfoID,
--	ISNULL([WRWorkRequest].[ProjectManager],'')AS ProjectManager,
--	ISNULL([WRWorkRequest].[ProjectPulseID],0)AS ProjectPulseID,
--	ISNULL([WRWorkRequest].[Requestor],'')AS Requestor,
----StartDate | datetime2
----SubmissionDate | datetime2
--	ISNULL([WRWorkRequest].[Supervisor],'')AS Supervisor,
--	ISNULL([WRWorkRequest].[WRNumber],'')AS WRNumber,
--	ISNULL([WRWorkRequest].[WRStatusID],0)AS WRStatusID,
--WHERE [IDENTITY COLUMN] = @ID
--END
--ELSE
--BEGIN
 
----USE TO RETURN A ZERO DATA SET IF THE ROW NOT IN SELECTED TABLE - SOMETIMES NEEDED FOR CLASS
 
--	SELECT
----AverageRate | decimal
--	0  AS BranchAssignmentID,
--	0  AS BranchID,
----CompletionDate | datetime2
----DatePrepared | datetime2
--	'01-JAN-1900'  AS DraftDueDate,
--	0  AS FinancialInfoID,
--	0  AS FY,
--	0  AS ID,
--	0  AS ProjectInfoID,
--	''  AS ProjectManager,
--	0  AS ProjectPulseID,
--	''  AS Requestor,
----StartDate | datetime2
----SubmissionDate | datetime2
--	''  AS Supervisor,
--	''  AS WRNumber,
--	0  AS WRStatusID,
--END