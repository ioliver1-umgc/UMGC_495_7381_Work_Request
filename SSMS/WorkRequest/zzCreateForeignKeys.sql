ALTER TABLE [dbo].[WRAllowableHour]  WITH CHECK ADD  CONSTRAINT [FK_AllowableHour_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[WRAllowableHour] CHECK CONSTRAINT [FK_AllowableHour_WorkRequest]
GO
ALTER TABLE [dbo].[WRAllowableHour]  WITH CHECK ADD  CONSTRAINT [FK_WRAllowableHour_WRDiscipline] FOREIGN KEY([DisciplineID])
REFERENCES [dbo].[WRDiscipline] ([ID])
GO
ALTER TABLE [dbo].[WRAllowableHour] CHECK CONSTRAINT [FK_WRAllowableHour_WRDiscipline]
GO
ALTER TABLE [dbo].[WRAssignedWorker]  WITH CHECK ADD  CONSTRAINT [FK_AssignedWorker_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[WRAssignedWorker] CHECK CONSTRAINT [FK_AssignedWorker_WorkRequest]
GO
ALTER TABLE [dbo].[WRAssignedWorker]  WITH CHECK ADD  CONSTRAINT [FK_WRAssignedWorker_WRDiscipline] FOREIGN KEY([DisciplineID])
REFERENCES [dbo].[WRDiscipline] ([ID])
GO
ALTER TABLE [dbo].[WRAssignedWorker] CHECK CONSTRAINT [FK_WRAssignedWorker_WRDiscipline]
GO
ALTER TABLE [dbo].[WRAssigneeDiscipline]  WITH CHECK ADD  CONSTRAINT [FK_AssigneeDiscipline_Assignee] FOREIGN KEY([AssigneeID])
REFERENCES [dbo].[WRAssignee] ([ID])
GO
ALTER TABLE [dbo].[WRAssigneeDiscipline] CHECK CONSTRAINT [FK_AssigneeDiscipline_Assignee]
GO
ALTER TABLE [dbo].[WRAssigneeDiscipline]  WITH CHECK ADD  CONSTRAINT [FK_AssigneeDiscipline_Discipline] FOREIGN KEY([DisciplineID])
REFERENCES [dbo].[WRDiscipline] ([ID])
GO
ALTER TABLE [dbo].[WRAssigneeDiscipline] CHECK CONSTRAINT [FK_AssigneeDiscipline_Discipline]
GO
ALTER TABLE [dbo].[WRAssignment]  WITH CHECK ADD  CONSTRAINT [FK_Assignment_Type] FOREIGN KEY([ParentID])
REFERENCES [dbo].[WRAssignment] ([ID])
GO
ALTER TABLE [dbo].[WRAssignment] CHECK CONSTRAINT [FK_Assignment_Type]
GO
ALTER TABLE [dbo].[WRBranchAssignment]  WITH CHECK ADD  CONSTRAINT [FK_BranchAssignment_Assignment] FOREIGN KEY([AssignmentID])
REFERENCES [dbo].[WRAssignment] ([ID])
GO
ALTER TABLE [dbo].[WRBranchAssignment] CHECK CONSTRAINT [FK_BranchAssignment_Assignment]
GO
ALTER TABLE [dbo].[WRBranchAssignment]  WITH CHECK ADD  CONSTRAINT [FK_BranchAssignment_Branch] FOREIGN KEY([BranchID])
REFERENCES [dbo].[WRBranch] ([ID])
GO
ALTER TABLE [dbo].[WRBranchAssignment] CHECK CONSTRAINT [FK_BranchAssignment_Branch]
GO
ALTER TABLE [dbo].[WRBranchAssignment]  WITH CHECK ADD  CONSTRAINT [FK_BranchAssignment_ResponsibleGroup] FOREIGN KEY([RespGroupID])
REFERENCES [dbo].[WRResponsibleGroup] ([ID])
GO
ALTER TABLE [dbo].[WRBranchAssignment] CHECK CONSTRAINT [FK_BranchAssignment_ResponsibleGroup]
GO
ALTER TABLE [dbo].[WRBranchSupervisor]  WITH CHECK ADD  CONSTRAINT [FK_Supervisor_ResponsibleGroup] FOREIGN KEY([ResponsibleGroupID])
REFERENCES [dbo].[WRResponsibleGroup] ([ID])
GO
ALTER TABLE [dbo].[WRBranchSupervisor] CHECK CONSTRAINT [FK_Supervisor_ResponsibleGroup]
GO
ALTER TABLE [dbo].[WRDiscipline]  WITH CHECK ADD  CONSTRAINT [FK_Discipline_Branch] FOREIGN KEY([BranchID])
REFERENCES [dbo].[WRBranch] ([ID])
GO
ALTER TABLE [dbo].[WRDiscipline] CHECK CONSTRAINT [FK_Discipline_Branch]
GO
ALTER TABLE [dbo].[WRFinancialInfo]  WITH CHECK ADD  CONSTRAINT [FK_FinancialInfo_OrgCode] FOREIGN KEY([Org_Code])
REFERENCES [dbo].[WROrgCode] ([Org_Code])
GO
ALTER TABLE [dbo].[WRFinancialInfo] CHECK CONSTRAINT [FK_FinancialInfo_OrgCode]
GO
ALTER TABLE [dbo].[WRHistory]  WITH CHECK ADD  CONSTRAINT [FK_History_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[WRHistory] CHECK CONSTRAINT [FK_History_WorkRequest]
GO
ALTER TABLE [dbo].[WRNumber]  WITH CHECK ADD  CONSTRAINT [FK_WRNumber_FiscalYear] FOREIGN KEY([FiscalYearID])
REFERENCES [dbo].[WRFiscalYear] ([ID])
GO
ALTER TABLE [dbo].[WRNumber] CHECK CONSTRAINT [FK_WRNumber_FiscalYear]
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
ALTER TABLE [dbo].[WRResponsibleBranchSupervisor]  WITH CHECK ADD  CONSTRAINT [FK_ResponsibleBranchSupervisor_ResponsibleGroup] FOREIGN KEY([ResponsibleGroupID])
REFERENCES [dbo].[WRResponsibleGroup] ([ID])
GO
ALTER TABLE [dbo].[WRResponsibleBranchSupervisor] CHECK CONSTRAINT [FK_ResponsibleBranchSupervisor_ResponsibleGroup]
GO
ALTER TABLE [dbo].[WRTaskComment]  WITH CHECK ADD  CONSTRAINT [FK_TaskComment_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[WRTaskComment] CHECK CONSTRAINT [FK_TaskComment_WorkRequest]
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
ALTER TABLE [dbo].[WRWorkRequestTravel]  WITH CHECK ADD  CONSTRAINT [FK_WorkRequestTravel_TravelCategory] FOREIGN KEY([TravelCategoryID])
REFERENCES [dbo].[WRTravelCategory] ([ID])
GO
ALTER TABLE [dbo].[WRWorkRequestTravel] CHECK CONSTRAINT [FK_WorkRequestTravel_TravelCategory]
GO
ALTER TABLE [dbo].[WRWorkRequestTravel]  WITH CHECK ADD  CONSTRAINT [FK_WorkRequestTravel_WorkRequest] FOREIGN KEY([WorkRequestID])
REFERENCES [dbo].[WRWorkRequest] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[WRWorkRequestTravel] CHECK CONSTRAINT [FK_WorkRequestTravel_WorkRequest]
GO
