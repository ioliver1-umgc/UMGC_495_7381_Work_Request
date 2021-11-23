USE [Pulse]
GO
ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_WRProjectInfo_WRWorkRequest]
GO
ALTER TABLE [dbo].[WRWorkRequest] DROP CONSTRAINT [FK_WRWorkRequest_WRFinancialInfo]
GO

ALTER TABLE [dbo].[WRWorkRequest] DROP CONSTRAINT [FK_WorkRequest_WRStatus]
GO

ALTER TABLE [dbo].[WRWorkRequest] DROP CONSTRAINT [FK_WorkRequest_BranchAssignment]
GO

/****** Object:  Table [dbo].[WRWorkRequest]    Script Date: 11/22/2021 8:14:15 PM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[WRWorkRequest]') AND type in (N'U'))
DROP TABLE [dbo].[WRWorkRequest]
GO

/****** Object:  Table [dbo].[WRWorkRequest]    Script Date: 11/22/2021 8:14:15 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[WRWorkRequest](
	[ID] [int] IDENTITY(1,1) NOT NULL,
[AverageRate] [decimal](18, 2) NULL,
[BranchAssignmentID] [int] NULL,
[BranchID] [int] NULL,
[CompletionDate] [datetime2](7) NOT NULL,
[DatePrepared] [datetime2](7) NOT NULL,
[DraftDueDate] [datetime] NULL,
[FinancialInfoID] [int] NULL,
[FY] [int] NULL,
[ProjectInfoID] [int] NOT NULL,
[ProjectManager] [varchar](255) NULL,
[ProjectPulseID] [int] NULL,
[Requestor] [varchar](255) NOT NULL,
[StartDate] [datetime2](7) NOT NULL,
[SubmissionDate] [datetime2](7) NULL,
[Supervisor] [varchar](255) NULL,
[WRNumber] [varchar](255) NOT NULL,
[WRStatusID] [int] NOT NULL,

 CONSTRAINT [PK_WorkRequest] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[WRWorkRequest]  WITH CHECK ADD  CONSTRAINT [FK_WorkRequest_BranchAssignment] FOREIGN KEY([BranchAssignmentID])
REFERENCES [dbo].[WRBranchAssignment] ([ID])
GO

ALTER TABLE [dbo].[WRWorkRequest] CHECK CONSTRAINT [FK_WorkRequest_BranchAssignment]
GO

ALTER TABLE [dbo].[WRWorkRequest]  WITH CHECK ADD  CONSTRAINT [FK_WorkRequest_WRStatus] FOREIGN KEY([WRStatusID])
REFERENCES [dbo].[WRStatus] ([ID])
GO

ALTER TABLE [dbo].[WRWorkRequest] CHECK CONSTRAINT [FK_WorkRequest_WRStatus]
GO

ALTER TABLE [dbo].[WRWorkRequest]  WITH CHECK ADD  CONSTRAINT [FK_WRWorkRequest_WRFinancialInfo] FOREIGN KEY([FinancialInfoID])
REFERENCES [dbo].[WRFinancialInfo] ([ID])
GO

ALTER TABLE [dbo].[WRWorkRequest] CHECK CONSTRAINT [FK_WRWorkRequest_WRFinancialInfo]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_WRProjectInfo_WRWorkRequest] FOREIGN KEY([WRID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_WRProjectInfo_WRWorkRequest]
GO


