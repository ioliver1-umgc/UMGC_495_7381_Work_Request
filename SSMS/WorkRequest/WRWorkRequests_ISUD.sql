SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO
CREATE  PROCEDURE uspWRWorkRequests_ISUD
		(
	@AverageRate DECIMAL,
	@BranchAssignmentID INT,
	@BranchID INT,
	@CompletionDate DATETIME2,
	@DatePrepared DATETIME2,
	@Documents VARCHAR(255),
	@DocumentsInstr VARCHAR(MAX),
	@DocumentsLocation VARCHAR(MAX),
	@DraftDueDate DATETIME,
	@DrChecks BIT,
	@ECESSubmissionDate DATETIME,
	@FinancialInfoID INT,
	@FY INT,
	@OtherDisciplines VARCHAR(255),
	@PA VARCHAR(255),
	@PMSharePointGroupID INT,
	@PreviousWRID INT,
	@ProgramAnalyst VARCHAR(255),
	@ProgramAnalystName VARCHAR(255),
	@ProjectInfoID INT,
	@ProjectManager VARCHAR(255),
	@ProjectManagerName VARCHAR(255),
	@ProjectPulseID INT,
	@RequestedFor VARCHAR(255),
	@RequestedForName VARCHAR(255),
	@Requestor VARCHAR(255),
	@RequestorName VARCHAR(255),
	@SpecificAssignmentInfo VARCHAR(MAX),
	@SpecificCostDistrInfo VARCHAR(MAX),
	@StartDate DATETIME2,
	@SubmissionDate DATETIME2,
	@Supervisor VARCHAR(255),
	@SupervisorName VARCHAR(255),
	@SupervisorSharePointGroupID INT,
	@WRWorkRequest_ID INT,
	@WRNumber VARCHAR(255),
	@WRStatusID INT,
	@WRUrl VARCHAR(MAX),
	@TYPE_ACTION VARCHAR(25)
	)
 
AS
 

-- =============================================
-- Author:		DESKTOP-KKJ5QGB\Will T
-- Create date: 2021/11/20
-- Description:	Shreds an XML document to relational table intended to Create, Update, or Delete rows in 'WRWorkRequests'
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
	UPDATE WRWorkRequests
	SET 
		AverageRate = @AverageRate,
		BranchAssignmentID = @BranchAssignmentID,
		BranchID = @BranchID,
		CompletionDate = @CompletionDate,
		DatePrepared = @DatePrepared,
		Documents = @Documents,
		DocumentsInstr = @DocumentsInstr,
		DocumentsLocation = @DocumentsLocation,
		DraftDueDate = @DraftDueDate,
		DrChecks = @DrChecks,
		ECESSubmissionDate = @ECESSubmissionDate,
		FinancialInfoID = @FinancialInfoID,
		FY = @FY,
		OtherDisciplines = @OtherDisciplines,
		PA = @PA,
		PMSharePointGroupID = @PMSharePointGroupID,
		PreviousWRID = @PreviousWRID,
		ProgramAnalyst = @ProgramAnalyst,
		ProgramAnalystName = @ProgramAnalystName,
		ProjectInfoID = @ProjectInfoID,
		ProjectManager = @ProjectManager,
		ProjectManagerName = @ProjectManagerName,
		ProjectPulseID = @ProjectPulseID,
		RequestedFor = @RequestedFor,
		RequestedForName = @RequestedForName,
		Requestor = @Requestor,
		RequestorName = @RequestorName,
		SpecificAssignmentInfo = @SpecificAssignmentInfo,
		SpecificCostDistrInfo = @SpecificCostDistrInfo,
		StartDate = @StartDate,
		SubmissionDate = @SubmissionDate,
		Supervisor = @Supervisor,
		SupervisorName = @SupervisorName,
		SupervisorSharePointGroupID = @SupervisorSharePointGroupID,
		--WorkRequest_ID = @WRWorkRequest_ID,
		WRNumber = @WRNumber,
		WRStatusID = @WRStatusID,
		WRUrl = @WRUrl
		where [WRWorkRequest_ID]=  @WRWorkRequest_ID
END
 
 
IF @TYPE_ACTION='INSERT'
BEGIN
	INSERT INTO WRWorkRequests
	(
	AverageRate,
	BranchAssignmentID,
	BranchID,
	CompletionDate,
	DatePrepared,
	Documents,
	DocumentsInstr,
	DocumentsLocation,
	DraftDueDate,
	DrChecks,
	ECESSubmissionDate,
	FinancialInfoID,
	FY,
	OtherDisciplines,
	PA,
	PMSharePointGroupID,
	PreviousWRID,
	ProgramAnalyst,
	ProgramAnalystName,
	ProjectInfoID,
	ProjectManager,
	ProjectManagerName,
	ProjectPulseID,
	RequestedFor,
	RequestedForName,
	Requestor,
	RequestorName,
	SpecificAssignmentInfo,
	SpecificCostDistrInfo,
	StartDate,
	SubmissionDate,
	Supervisor,
	SupervisorName,
	SupervisorSharePointGroupID,
	--WorkRequest_ID,  not needed, it's identity
	WRNumber,
	WRStatusID,
	WRUrl
	)
VALUES
(
	@AverageRate,
	@BranchAssignmentID,
	@BranchID,
	@CompletionDate,
	@DatePrepared,
	@Documents,
	@DocumentsInstr,
	@DocumentsLocation,
	@DraftDueDate,
	@DrChecks,
	@ECESSubmissionDate,
	@FinancialInfoID,
	@FY,
	@OtherDisciplines,
	@PA,
	@PMSharePointGroupID,
	@PreviousWRID,
	@ProgramAnalyst,
	@ProgramAnalystName,
	@ProjectInfoID,
	@ProjectManager,
	@ProjectManagerName,
	@ProjectPulseID,
	@RequestedFor,
	@RequestedForName,
	@Requestor,
	@RequestorName,
	@SpecificAssignmentInfo,
	@SpecificCostDistrInfo,
	@StartDate,
	@SubmissionDate,
	@Supervisor,
	@SupervisorName,
	@SupervisorSharePointGroupID,
	--@WRWorkRequest_ID, not needed, it's identity
	@WRNumber,
	@WRStatusID,
	@WRUrl
)
END
--END CODE HERE--
