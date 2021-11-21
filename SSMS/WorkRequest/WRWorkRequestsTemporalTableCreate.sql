USE [Pulse]
GO

ALTER TABLE [dbo].[WRAssignedWorker] DROP CONSTRAINT [FK_AssignedWorker_WorkRequest]
GO

ALTER TABLE [dbo].[WRAllowableHour] DROP CONSTRAINT [FK_AllowableHour_WorkRequest]
GO

/****** Object:  Table [dbo].[WRWorkRequests]    Script Date: 11/21/2021 1:16:22 PM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[WRWorkRequests]') AND type in (N'U'))
DROP TABLE [dbo].[WRWorkRequests]
GO

/****** Object:  Table [dbo].[WRWorkRequests]    Script Date: 11/21/2021 1:16:22 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[WRWorkRequests](
	[WRWorkRequest_ID] [int] IDENTITY(1,1) NOT NULL,
	[WRNumber] [varchar](255) NOT NULL,
	[WRStatusID] [int] NOT NULL,
	[FY] [int] NULL,
	[BranchID] [int] NULL,
	[DatePrepared] [datetime2](7) NOT NULL,
	[Requestor] [varchar](255) NOT NULL,
	[RequestorName] [varchar](255) NULL,
	[RequestedFor] [varchar](255) NULL,
	[RequestedForName] [varchar](255) NULL,
	[ProjectManager] [varchar](255) NULL,
	[ProjectManagerName] [varchar](255) NULL,
	[PA] [varchar](255) NULL,
	[ProgramAnalyst] [varchar](255) NULL,
	[ProgramAnalystName] [varchar](255) NULL,
	[PMSharePointGroupID] [int] NULL,
	[DrChecks] [bit] NOT NULL,
	[PreviousWRID] [int] NULL,
	[ProjectInfoID] [int] NOT NULL,
	[ProjectPulseID] [int] NULL,
	[FinancialInfoID] [int] NULL,
	[BranchAssignmentID] [int] NULL,
	[SpecificAssignmentInfo] [varchar](max) NULL,
	[Supervisor] [varchar](255) NULL,
	[SupervisorName] [varchar](255) NULL,
	[SupervisorSharePointGroupID] [int] NULL,
	[StartDate] [datetime2](7) NOT NULL,
	[CompletionDate] [datetime2](7) NOT NULL,
	[Documents] [varchar](255) NOT NULL,
	[DocumentsInstr] [varchar](max) NULL,
	[OtherDisciplines] [varchar](255) NULL,
	[SpecificCostDistrInfo] [varchar](max) NULL,
	[SubmissionDate] [datetime2](7) NULL,
	[WRUrl] [varchar](max) NULL,
	[DocumentsLocation] [varchar](max) NOT NULL,
	[AverageRate] [decimal](18, 2) NULL,
	[ECESSubmissionDate] [datetime] NULL,
	[DraftDueDate] [datetime] NULL,
--  SysStartTime DATETIME2 GENERATED ALWAYS AS ROW START NOT NULL
 -- , SysEndTime DATETIME2 GENERATED ALWAYS AS ROW END NOT NULL
 -- , PERIOD FOR SYSTEM_TIME (SysStartTime, SysEndTime),
 CONSTRAINT [PK_WorkRequest] PRIMARY KEY CLUSTERED 
(
	[WRWorkRequest_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
--WITH (SYSTEM_VERSIONING = ON (HISTORY_TABLE = dbo.WRWorkRequestsHistory));
GO




--Readd foreign keys to parent tables
ALTER TABLE [dbo].[WRAllowableHour]  WITH CHECK ADD  CONSTRAINT [FK_AllowableHour_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequests] ([WRWorkRequest_ID])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[WRAllowableHour] CHECK CONSTRAINT [FK_AllowableHour_WorkRequest]
GO

ALTER TABLE [dbo].[WRAssignedWorker]  WITH CHECK ADD  CONSTRAINT [FK_AssignedWorker_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequests] ([WRWorkRequest_ID])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[WRAssignedWorker] CHECK CONSTRAINT [FK_AssignedWorker_WorkRequest]
GO

--Remove
ALTER TABLE [dbo].WRWorkRequests SET ( SYSTEM_VERSIONING = OFF)
GO
DROP TABLE [dbo].[WRWorkRequests]
GO
DROP TABLE [dbo].[WRWorkRequestsHistory]
GO