USE [Pulse]
GO

ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_WRProjectInfo_WRWorkRequest]
GO

ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_ProjectInfo_Section]
GO

ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_ProjectInfo_Location]
GO

ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_ProjectInfo_FundSource]
GO

ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_ProjectInfo_Country]
GO

ALTER TABLE [dbo].[WRProjectInfo] DROP CONSTRAINT [FK_ProjectInfo_CityRegion]
GO

/****** Object:  Table [dbo].[WRProjectInfo]    Script Date: 11/22/2021 8:18:41 PM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[WRProjectInfo]') AND type in (N'U'))
DROP TABLE [dbo].[WRProjectInfo]
GO

/****** Object:  Table [dbo].[WRProjectInfo]    Script Date: 11/22/2021 8:18:41 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[WRProjectInfo](
[Background] [varchar](max) NULL,
[BranchId] [int] NULL,
[ContinentID] [int] NULL,
[ContractNumber] [varchar](255) NULL,
[DirWR] [int] NOT NULL,
[FundSourceID] [int] NULL,
[FundSourceText] [varchar](255) NULL,
[InstallationID] [int] NULL,
[LocationID] [int] NULL,
[MilitarySiteID] [int] NULL,
[OrgCode] [varchar](255) NULL,
[ProgramYear] [varchar](255) NULL,
[ProjectName] [varchar](255) NULL,
[ProjectNotes] [varchar](max) NULL,
[ProjectNumber] [varchar](255) NULL,
[PulseCityRegionID] [int] NULL,
[PulseCountryID] [int] NULL,
[SectionId] [int] NULL,
[Site] [varchar](255) NULL,
[WRID] [int] IDENTITY(1,1) NOT NULL,

 CONSTRAINT [PK_ProjectInfo] PRIMARY KEY CLUSTERED 
(
	[WRID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_ProjectInfo_CityRegion] FOREIGN KEY([PulseCityRegionID])
REFERENCES [dbo].[PulseCityRegion] ([Id])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_ProjectInfo_CityRegion]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_ProjectInfo_Country] FOREIGN KEY([PulseCountryID])
REFERENCES [dbo].[PulseCountry] ([Id])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_ProjectInfo_Country]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_ProjectInfo_FundSource] FOREIGN KEY([FundSourceID])
REFERENCES [dbo].[WRFundSource] ([ID])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_ProjectInfo_FundSource]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_ProjectInfo_Location] FOREIGN KEY([LocationID])
REFERENCES [dbo].[WRLocation] ([ID])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_ProjectInfo_Location]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_ProjectInfo_Section] FOREIGN KEY([SectionId])
REFERENCES [dbo].[WRSection] ([ID])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_ProjectInfo_Section]
GO

ALTER TABLE [dbo].[WRProjectInfo]  WITH CHECK ADD  CONSTRAINT [FK_WRProjectInfo_WRWorkRequest] FOREIGN KEY([WRID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
GO

ALTER TABLE [dbo].[WRProjectInfo] CHECK CONSTRAINT [FK_WRProjectInfo_WRWorkRequest]
GO


