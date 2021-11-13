SET QUOTED_IDENTIFIER ON;
GO
SET ANSI_NULLS ON;
GO
alter PROCEDURE uspWorkRequest_CRUD
(
    @AverageRate DECIMAL,
    @BranchAssignmentID INT,
    @CompletionDate DATETIME2,
    @DatePrepared DATETIME2,
    @Documents VARCHAR(255),
    @DocumentsInstr VARCHAR(MAX),
    @DocumentsLocation VARCHAR(MAX),
    @DraftDueDate DATETIME,
    @DrChecks BIT,
    @ECESSubmissionDate DATETIME,
    @ID INT,
    @OtherDisciplines VARCHAR(255),
    @PMSharePointGroupID INT,
    @PreviousWRID INT,
    @ProjectInfoID INT,
    @ProjectManager VARCHAR(255),
    @ProjectManagerDisplayName VARCHAR(255),
    @ProjectManagerEmail VARCHAR(255),
    @ProjectManagerPhone VARCHAR(255),
    @RequestedFor VARCHAR(255),
    @RequestedForDisplayName VARCHAR(255),
    @RequestedForEmail VARCHAR(255),
    @RequestedForPhone VARCHAR(255),
    @Requestor VARCHAR(255),
    @RequestorDisplayName VARCHAR(255),
    @RequestorEmail VARCHAR(255),
    @RequestorPhone VARCHAR(255),
    --@RowVersion TIMESTAMP, This is automatic
    @SpecificAssignmentInfo VARCHAR(MAX),
    @SpecificCostDistrInfo VARCHAR(MAX),
    @StartDate DATETIME2,
    @SubmissionDate DATETIME2,
    @Supervisor VARCHAR(255),
    @SupervisorDisplayName VARCHAR(255),
    @SupervisorEmail VARCHAR(255),
    @SupervisorPhone VARCHAR(255),
    @SupervisorSharePointGroupID INT,
    @WRNumber VARCHAR(255),
    @WRStatusID INT,
    @WRUrl VARCHAR(MAX),
	@TYPE_ACTION VARCHAR(25)--Used to dictate what happens when the SP is called.
)
AS


-- =============================================
-- Author:		MUCHACHOLAPTOP\dave_
-- Create date: 2021/11/12
-- Description:	Shreds an XML document to relational table intended to Create, Update, or Delete rows in 'WorkRequest'
-- After running do the following: 
-- INSERT fixes:
-- 1. Replace TABLE with the ID of the target table in entire procedure
-- 2. Remove comma after the last column in the CREATE Table statement
-- 3. Remove <sql_system_user> from last_update_date BY_WHO as appropriate from test data/ SELECT in INSERT. 
-- 4. Remove all computed columns from the CREATE/INSERT/SELECT statements and test XML
-- 5. Remove the TABLE_ID Column from the CREATE/INSERT/SELECT statements
-- 6. Set correct dates/non nulls in test data. Remove all other bugs I wasn't smart enough TO find
-- UPDATE fixes:
-- 1. Replace TABLE_ID with ID of target table
-- 2. Remove comma after the last column in the SET column list.
-- 3. PROD.LAST_UDPATE_DATE = GETDATE(), SQL_SYSTEM_USER = SYSTEM_USER, BY_WHO= @BY_WHO
-- 4. Remove the update to the TABLE_ID
-- DELETE fixes:
-- =============================================
--START CODE HERE--
IF @TYPE_ACTION='CREATE'
BEGIN
	INSERT INTO WorkRequest
	(
	AverageRate,
	BranchAssignmentID,
	CompletionDate,
	DatePrepared,
	Documents,
	DocumentsInstr,
	DocumentsLocation,
	DraftDueDate,
	DrChecks,
	ECESSubmissionDate,
	--ID,
	OtherDisciplines,
	PMSharePointGroupID,
	PreviousWRID,
	ProjectInfoID,
	ProjectManager,
	ProjectManagerDisplayName,
	ProjectManagerEmail,
	ProjectManagerPhone,
	RequestedFor,
	RequestedForDisplayName,
	RequestedForEmail,
	RequestedForPhone,
	Requestor,
	RequestorDisplayName,
	RequestorEmail,
	RequestorPhone,
	--RowVersion,
	SpecificAssignmentInfo,
	SpecificCostDistrInfo,
	StartDate,
	SubmissionDate,
	Supervisor,
	SupervisorDisplayName,
	SupervisorEmail,
	SupervisorPhone,
	SupervisorSharePointGroupID,
	WRNumber,
	WRStatusID,
	WRUrl
	)
VALUES
(
	@AverageRate,
	@BranchAssignmentID,
	@CompletionDate,
	@DatePrepared,
	@Documents,
	@DocumentsInstr,
	@DocumentsLocation,
	@DraftDueDate,
	@DrChecks,
	@ECESSubmissionDate,
	--@ID,
	@OtherDisciplines,
	@PMSharePointGroupID,
	@PreviousWRID,
	@ProjectInfoID,
	@ProjectManager,
	@ProjectManagerDisplayName,
	@ProjectManagerEmail,
	@ProjectManagerPhone,
	@RequestedFor,
	@RequestedForDisplayName,
	@RequestedForEmail,
	@RequestedForPhone,
	@Requestor,
	@RequestorDisplayName,
	@RequestorEmail,
	@RequestorPhone,
	--@RowVersion,
	@SpecificAssignmentInfo,
	@SpecificCostDistrInfo,
	@StartDate,
	@SubmissionDate,
	@Supervisor,
	@SupervisorDisplayName,
	@SupervisorEmail,
	@SupervisorPhone,
	@SupervisorSharePointGroupID,
	@WRNumber,
	@WRStatusID,
	@WRUrl
)
end
IF @TYPE_ACTION = 'READ'
BEGIN
SELECT ID,
       WRNumber,
       WRStatusID,
       DatePrepared,
       Requestor,
       RequestedFor,
       ProjectManager,
       PMSharePointGroupID,
       DrChecks,
       PreviousWRID,
       ProjectInfoID,
       BranchAssignmentID,
       SpecificAssignmentInfo,
       Supervisor,
       SupervisorSharePointGroupID,
       StartDate,
       CompletionDate,
       Documents,
       DocumentsInstr,
       OtherDisciplines,
       SpecificCostDistrInfo,
       SubmissionDate,
       RowVersion,
       ProjectManagerDisplayName,
       SupervisorDisplayName,
       WRUrl,
       RequestorDisplayName,
       RequestorPhone,
       RequestorEmail,
       RequestedForDisplayName,
       RequestedForPhone,
       RequestedForEmail,
       ProjectManagerPhone,
       ProjectManagerEmail,
       SupervisorPhone,
       SupervisorEmail,
       DocumentsLocation,
       AverageRate,
       ECESSubmissionDate,
       DraftDueDate FROM WorkRequest

WHERE [ID] = @ID
END
IF @TYPE_ACTION = 'UPDATE'
BEGIN
    UPDATE WorkRequest
    SET AverageRate = @AverageRate,
        BranchAssignmentID = @BranchAssignmentID,
        CompletionDate = @CompletionDate,
        DatePrepared = @DatePrepared,
        Documents = @Documents,
        DocumentsInstr = @DocumentsInstr,
        DocumentsLocation = @DocumentsLocation,
        DraftDueDate = @DraftDueDate,
        DrChecks = @DrChecks,
        ECESSubmissionDate = @ECESSubmissionDate,
        --ID = @ID,
        OtherDisciplines = @OtherDisciplines,
        PMSharePointGroupID = @PMSharePointGroupID,
        PreviousWRID = @PreviousWRID,
        ProjectInfoID = @ProjectInfoID,
        ProjectManager = @ProjectManager,
        ProjectManagerDisplayName = @ProjectManagerDisplayName,
        ProjectManagerEmail = @ProjectManagerEmail,
        ProjectManagerPhone = @ProjectManagerPhone,
        RequestedFor = @RequestedFor,
        RequestedForDisplayName = @RequestedForDisplayName,
        RequestedForEmail = @RequestedForEmail,
        RequestedForPhone = @RequestedForPhone,
        Requestor = @Requestor,
        RequestorDisplayName = @RequestorDisplayName,
        RequestorEmail = @RequestorEmail,
        RequestorPhone = @RequestorPhone,
        --RowVersion = @RowVersion,
        SpecificAssignmentInfo = @SpecificAssignmentInfo,
        SpecificCostDistrInfo = @SpecificCostDistrInfo,
        StartDate = @StartDate,
        SubmissionDate = @SubmissionDate,
        Supervisor = @Supervisor,
        SupervisorDisplayName = @SupervisorDisplayName,
        SupervisorEmail = @SupervisorEmail,
        SupervisorPhone = @SupervisorPhone,
        SupervisorSharePointGroupID = @SupervisorSharePointGroupID,
        WRNumber = @WRNumber,
        WRStatusID = @WRStatusID,
        WRUrl = @WRUrl
		WHERE @ID = ID;
END;

IF @TYPE_ACTION = 'DELETE'
BEGIN
    DELETE FROM dbo.WorkRequest
		WHERE @ID = ID;


END;



--END CODE HERE--