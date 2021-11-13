SET QUOTED_IDENTIFIER ON;
GO
SET ANSI_NULLS ON;
GO
ALTER PROCEDURE WorkRequest_R @ID INT = 1
AS

/*	
	<GENERAL_INFO>
		<AUTHOR>
		MUCHACHOLAPTOP\dave_
		</AUTHOR>
		<DATECREATED>
		Nov 12 2021  1:20PM
		</DATECREATED>
		<FILENAME>
		WorkRequest_R
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

IF
(
    SELECT COUNT(*)FROM WorkRequest
) > 0
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
           DraftDueDate
    FROM WorkRequest
    WHERE [ID] = @ID;
END;
ELSE
BEGIN

    --USE TO RETURN A ZERO DATA SET IF THE ROW NOT IN SELECTED TABLE - SOMETIMES NEEDED FOR CLASS

    SELECT
        --AverageRate | decimal
        0 AS BranchAssignmentID,
        --CompletionDate | datetime2
        --DatePrepared | datetime2
        '' AS Documents,
        '' AS DocumentsInstr,
        '' AS DocumentsLocation,
        '01-JAN-1900' AS DraftDueDate,
        --DrChecks | bit
        '01-JAN-1900' AS ECESSubmissionDate,
        0 AS ID,
        '' AS OtherDisciplines,
        0 AS PMSharePointGroupID,
        0 AS PreviousWRID,
        0 AS ProjectInfoID,
        '' AS ProjectManager,
        '' AS ProjectManagerDisplayName,
        '' AS ProjectManagerEmail,
        '' AS ProjectManagerPhone,
        '' AS RequestedFor,
        '' AS RequestedForDisplayName,
        '' AS RequestedForEmail,
        '' AS RequestedForPhone,
        '' AS Requestor,
        '' AS RequestorDisplayName,
        '' AS RequestorEmail,
        '' AS RequestorPhone,
        --RowVersion | timestamp
        '' AS SpecificAssignmentInfo,
        '' AS SpecificCostDistrInfo,
        --StartDate | datetime2
        --SubmissionDate | datetime2
        '' AS Supervisor,
        '' AS SupervisorDisplayName,
        '' AS SupervisorEmail,
        '' AS SupervisorPhone,
        0 AS SupervisorSharePointGroupID,
        '' AS WRNumber,
        0 AS WRStatusID,
        '' AS WRUrl;
END;