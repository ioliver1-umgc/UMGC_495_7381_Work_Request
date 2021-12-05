--WRBranchAssignment must be what ties together WRWorkRequest, WRAssignment, WRBranch to show information in the AssignmentType Tab. The BranchAssignment ID
--use this to build the Asignment drop downs:
select * from [dbo].[vwAsgnHierarchy] 
order by categid, typeid, subtypeid
--All this is is a Self Referenced table that allows the user to select up to 3 levels down: Category, Type, Subtype.
--Assignment Category
select * from [dbo].[WRAssignment]  --ParenID is self-referencing to ID

--Assignment Type
select * from [dbo].[WRAssignment]
order by parentid

--Assignment SubType
select * from [dbo].[WRAssignment]
order by parentid
select * from [dbo].[WRBranchAssignment]


