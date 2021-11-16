USE [Pulse]
GO
/****** Object:  StoredProcedure [dbo].[uspWRUser_ISUD]    Script Date: 11/16/2021 3:09:28 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[uspWRUser_ISUD]
(
    @Country_Code VARCHAR(2),
    @Department VARCHAR(255),
    @Display_Name VARCHAR(255),
    @DisplayName_Official VARCHAR(255),
    @Email VARCHAR(255),
    @Emp_ID_NO VARCHAR(9) = 1,
    @Emp_Type VARCHAR(255),
    @First_Name VARCHAR(255),
    @FLSA VARCHAR(1),
    @Last_Name VARCHAR(255),
    @LoginDomain VARCHAR(255),
    @LoginName VARCHAR(255),
    @Office_Symbol VARCHAR(50),
    @Org_Code VARCHAR(8),
    @Phone VARCHAR(255),
    @Position_Title VARCHAR(255),
    @SharePointGroupId INT,
    @TYPE_ACTION VARCHAR(25) = 'READ',
	@WRUSer_ID int
)
AS
BEGIN

    -- =============================================
    -- Author:		MUCHACHOLAPTOP\dave_
    -- Create date: 2021/11/16
    -- Description:	Shreds an XML document to relational table intended to Create, Update, or Delete rows in 'WRUser'
    -- After running do the following: 
    -- INSERT fixes:
    -- 1. Replace TABLE with the ID of the target table in entire procedure
    -- 2. Remove comma after the last column in the CREATE Table statement
    -- 3. Remove <sql_system_user> from last_update_date BY_WHO as appropriate from test data/ SELECT in INSERT. 
    -- 4. Remove all computed columns from the CREATE/INSERT/SELECT statements and test XML
    -- 5. Remove the TABLE_ID Column from the CREATE/INSERT/SELECT statements
    -- 6. Add TYPE_ACTION to SP Variables, add a BEGIN after the AS in the ISUD Proc
    -- 6. Set correct dates/non nulls in test data. Remove all other bugs I wasn't smart enough TO find
    -- UPDATE fixes:
    -- 1. Replace TABLE_ID with ID of target table
    -- 2. Remove comma after the last column in the SET column list.
    -- 3. PROD.LAST_UDPATE_DATE = GETDATE(), SQL_SYSTEM_USER = SYSTEM_USER, BY_WHO= @BY_WHO
    -- 4. Remove the update to the TABLE_ID
    -- DELETE fixes:
    -- =============================================
    --START CODE HERE--
    IF @TYPE_ACTION = 'UPDATE'
    BEGIN
        UPDATE WRUser
        SET Country_Code = @Country_Code,
            Department = @Department,
            Display_Name = @Display_Name,
            DisplayName_Official = @DisplayName_Official,
            Email = @Email,
            Emp_ID_NO = @Emp_ID_NO,
            Emp_Type = @Emp_Type,
            First_Name = @First_Name,
            FLSA = @FLSA,
            Last_Name = @Last_Name,
            LoginDomain = @LoginDomain,
            LoginName = @LoginName,
            Office_Symbol = @Office_Symbol,
            Org_Code = @Org_Code,
            Phone = @Phone,
            Position_Title = @Position_Title,
            SharePointGroupId = @SharePointGroupId
        WHERE WRUSer_ID = @WRUSer_ID  ;
    END;
    IF @TYPE_ACTION = 'INSERT'
    BEGIN
        INSERT INTO WRUser
        (
            Country_Code,
            Department,
            Display_Name,
            DisplayName_Official,
            Email,
            Emp_ID_NO,
            Emp_Type,
            First_Name,
            FLSA,
            Last_Name,
            LoginDomain,
            LoginName,
            Office_Symbol,
            Org_Code,
            Phone,
            Position_Title,
            SharePointGroupId
        )
        VALUES
        (@Country_Code, @Department, @Display_Name, @DisplayName_Official, @Email, @Emp_ID_NO, @Emp_Type, @First_Name,
         @FLSA, @Last_Name, @LoginDomain, @LoginName, @Office_Symbol, @Org_Code, @Phone, @Position_Title,
         @SharePointGroupId);
    END;
END;
--END CODE HERE--

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



