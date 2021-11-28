/*
<GENERAL_INFO>
		<AUTHOR>
			David A. Leake / Dan Dewart
		</AUTHOR>
		<DATECREATED>
		  	20211111
		</DATECREATED>
		<FILENAME>
			CreateASPNetAndXML_CRUDFromTable
		</FILENAME>
		<APPNAME>
			Student Database
		</APPNAME>
		<DB>
			WARCEN
		</DB>
		<SERVER>DEV_SQL</SERVER>
		<CALLEDBY NAME="" TYPE="" />
		<CALLS NAME="" TYPE="" />
		<DESC>
			THIS SCRIPT GENERATES CODE THAT CREATES A VB NET 'ENTITY' CLASS TO REPRESENT A SQL DATABASE TABLE
			OR A SQL VIEW. THH SCRIPT WILL GENERATE CLASS PROPERTIES FOR ALL TABLE COLUMNS.THE SCRIPT USES 
			THE STRUCTURE OF THE SQL TABLE OR VIEW TABLES TO GENERATE THE PROPERTIES.
			
			THE CODE GENERATES TWO GENERIC FUNCTIONS; ONE TO GET ENTITY DATA AND ANOTHER TO UPDATE ENTITY DATA. THE
			NAME OF THE SPROCS TO PERFORM THESE ACTIONS IS GENERATED IN THIS CODE, TO INCLUDE CREATE,UPDATE, DELETE OF TARGET TABLE			
			
			1. ANSWER QUESTIONS 1-10 IN SECTION --***SET THESE VALUES BEFORE RUNNING THIS QUERY***--
			
			2. SELECT PROPER DATABASE AND RUN SCRIPT TO GENERATE THE .NET CODE
			
			3. COPY AND PASTE INTO YOUR VB NET PROJECT
			
			4. LOOK FOR THE BLUE SQUIGGLY LINES AND CORRECT ERRORS
			
			5. MAKE OTHER ADJUSTMENTS AS NECESSARY
			
			*** IMPORTANT ***
			THIS IS A GENERIC CODE CREATOR. YOU WILL NEED TO REVIEW THE OUTPUT AND MAKE ADJUSTMENTS WHERE NECESSARY
			 
		</DESC>		
		<!--LIST OF CHANGES SUBMITTED-->
		<CHANGE BY="" DATE="">
			<DESC></DESC>
		</CHANGE>
		<!--[OPTIONAL] SAMPLE CODE IF ANY / ONLY NEEDED BY CLASSES OR PROCEDURES-->
		<SAMPLE>
 			<![CDATA[ Write sample code here: ]]>
		</SAMPLE>
	</GENERAL_INFO>
*/
SET NOCOUNT ON
GO

--DROP THE TEMP RESULT TABLE IF IT EXISTS
IF EXISTS ( SELECT  *
            FROM    sysobjects
            WHERE   id = OBJECT_ID(N'[dbo].[zzTemp_ClassCreator]')
                    AND OBJECTPROPERTY(id,N'IsTable') = 1 )
    DROP TABLE [dbo].[zzTemp_ClassCreator] 
GO

--***SET THESE VALUES BEFORE RUNNING THIS QUERY***--
DECLARE @CLASS_NAME VARCHAR(255)
DECLARE @DATABASE_UPDATE_HANDLER_CLASS_NAME VARCHAR(255)
DECLARE @DATABASE_HANDLER_INSTANTIATED_INSTANCE_NAME VARCHAR(255)
DECLARE @DATABASE_UPDATE_HANDLER_PROCEDURE VARCHAR(255)
DECLARE @SPROC_FOR_UPDATES VARCHAR(255)
DECLARE @SPROC_FOR_GETTNG_ENTITY_DATA VARCHAR(255)
DECLARE @DATABASE_HANDLER_GET_SERVICE_DATA_PROCEDURE VARCHAR(255)
DECLARE @ENTITY_ID_VARIABLE VARCHAR(255)
DECLARE @ENTITY_ID_VARIABLE_TYPE VARCHAR(255)
DECLARE @CREATE_COMMENT_REGIONS AS BIT
DECLARE @ENTITY_TABLE_OR_VIEW_NAME VARCHAR(255)
DECLARE @CREATE_SPROC_FRAMEWORK AS BIT
DECLARE @CREATE_ENTIRE_CLASS AS BIT

	--1. WHAT DO YOU WANT TO CALL THE CLASS THIS SCRIPT WILL CREATE?   
SET @CLASS_NAME = 'test'

	--2. WHAT IS THE NAME OF THE DATABASE HANDLER CLASS THAT HAS THE FUNCTION/SUB THAT CONNECTS TO THE DATABASE?   
SET @DATABASE_UPDATE_HANDLER_CLASS_NAME = 'DataBaseHandler'
	
	--3. WHAT SHOULD THE NAME OF THE INSTANTIATED ENTITY OF THE DATABASE HANDLER IN THE ENTITY CLASS BE?
SET @DATABASE_HANDLER_INSTANTIATED_INSTANCE_NAME = 'oDatabaseHandler'

	--4. WHAT IS THE NAME OF THE FUNCTION OR SUB IN THE DATABASE HANDLER THAT EXECUTE THE PASSED SQL COMMAND OBJECT?
SET @DATABASE_UPDATE_HANDLER_PROCEDURE = 'ExecuteSqlCommand'
	
	--5. WHAT IS THE NAME OF THE KEY IDENTIFIER FOR THE ENTITY NEEDED FOR FINDING THE DISTINCT ROW IN THE 'ENTITY' TABLE
SET @ENTITY_ID_VARIABLE = 'ID'

	--6. WHAT IS THE TYPE OF THE KEY IDENTIFIER FOR THE ENTITY NEEDED FOR FINDING THE DISTINCT ROW IN THE 'ENTITY' TABLE
SET @ENTITY_ID_VARIABLE_TYPE = 'INT'

	--7. WHAT IS THE NAME OF THE SQL STORED PROCEDURE THAT WILL BE CALLED BY THE DATA BASE HANDLER TO UPDATE THE ENTITY DATA TABLE?
SET @SPROC_FOR_UPDATES = 'uspWorkRequest_CUD'

	--8. WHAT IS THE NAME OF THE SQL STORED PROCEDURE THAT WILL BE CALLED BY THE DATA BASE HANDLER TO RETRIEVE THE ENTITY DATA?
SET @SPROC_FOR_GETTNG_ENTITY_DATA = 'uspWorkRequest_R'

	--9. DO YOU WANT TO CREATE THE COMMENTS REGIONS FOR EACH SUB AND PROPERTY DECLARATION? (0=NO, 1=YES)	
SET @CREATE_COMMENT_REGIONS = 1
	
	--10. TABLE NAME TO CREATE CLASS FOR
SET @ENTITY_TABLE_OR_VIEW_NAME = '''WorkRequest'''
	
	--11. DO YOU WANT TO CREATE THE ENTIRE CLASS. SET TO 0 IF YOU WANT TO JUST CREATE, E.G., THE SPROC FRAMEWORK
SET @CREATE_ENTIRE_CLASS = 1

	--11. DO YOU WANT TO CREATE THE FRAMEWORK CODE FOR THE SPROCS? CODE WILL BE INSERTED AT VERY END OF RESULTS...
SET @CREATE_SPROC_FRAMEWORK = 1

	--FOLLOWING CAN GENERALLY BE LEFT AT THEIR DEFAULT VALUES.

	--ADD CUSTOM STRING CHECKING PROCEDURE? MUST INSERT THIS INTO THE RESULT TABLE
	--AS THE VAR LOSES SCOPE IN THE CURSOR!
DECLARE @STRING_ERROR_CHECKING AS CHAR(1)
SET @STRING_ERROR_CHECKING = 1

	--BEGIN: THESE ARE THE INDIVIUDAL CODE GENERATOR BITS FOR CREATING SELECTED SECTIONS OF THE CLASS	
	-- OVERIDDEN IF THE @CREATE_ENTIRE_CLASS IS SET TO 1
		--CREATE PROPERTIES VB NET CODE?
DECLARE @CREATE_PROPERTIES AS BIT
SET @CREATE_PROPERTIES = 0
	
		--CREATE INITIALIZATION VB NET CODE?
DECLARE @CREATE_INITIALIZATION AS BIT
SET @CREATE_INITIALIZATION = 0
	
		--CREATE PARAMATARIZED CALL UPDATE IN DATABASE HANDLER VB NET CODE?
DECLARE @CREATE_CALL_PARAMATERIZEDUPDATE_PROCEDURE_FROM_CLASS AS BIT
SET @CREATE_CALL_PARAMATERIZEDUPDATE_PROCEDURE_FROM_CLASS = 0
	
		--CREATE PARAMATARIZED UPDATE PROCEDURE IN DATABASE HANDLER VB NET CODE?
DECLARE @CREATE_PARAMATERIZEDUPDATE_PROCEDURE AS BIT
SET @CREATE_PARAMATERIZEDUPDATE_PROCEDURE = 0
	
		--GENERATE THE CODE FOR DATABASEHANDLER UPDATE PROCEDURE?
DECLARE @GENERATE_UPDATE_PROCEDURE AS CHAR(1)
SET @GENERATE_UPDATE_PROCEDURE = 0
	--END: THESE ARE THE INDIVIUDAL CODE GENERATOR BITS FOR CREATING SELECTED SECTIONS OF THE CLASS	

--***END SET THESE VALUES BEFORE RUNNING THIS QUERY***--

DECLARE @COLUMN_NAME VARCHAR(128)
DECLARE @COLUMN_NAME2 VARCHAR(128)
DECLARE @LEN INT
DECLARE @VBNET_TYPE VARCHAR(32)
DECLARE @SQL_TYPE VARCHAR(32)
DECLARE @STRING_CHECKING CHAR(1)
DECLARE @TABLE_NAME VARCHAR(128)
DECLARE @TABLE_NAME2 VARCHAR(128)

--***START MAIN PROCESS***--
DECLARE @CMD VARCHAR(8000)
SET @CMD = '
SELECT DISTINCT
COLUMN_NAME,
	CHARACTER_MAXIMUM_LENGTH AS LENGTH,
	CASE DATA_TYPE
		WHEN ''char'' THEN ''String''
		WHEN ''datetime'' THEN ''Date''
		WHEN ''int'' THEN ''integer''
		WHEN ''numeric'' THEN ''long''
		WHEN ''nvarchar'' THEN ''String''
		WHEN ''real'' THEN ''long''
		WHEN ''smallint'' THEN ''integer''
		WHEN ''tinyint'' THEN ''integer''
		WHEN ''varchar'' THEN ''String''
		ELSE ''?''
	END AS	VBNET_TYPE,
	DATA_TYPE AS SQL_TYPE,
	TABLE_NAME,
	' + @STRING_ERROR_CHECKING + ' AS STRING_CHECKING
INTO zzTemp_ClassCreator
FROM information_schema.columns
WHERE TABLE_NAME IN (' + @ENTITY_TABLE_OR_VIEW_NAME + ')
'
EXEC(@CMD)

IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************START CLASS ****************************************************'

IF @CREATE_ENTIRE_CLASS = 1
BEGIN

    PRINT 'Imports DataBaseHandler'
    PRINT 'Imports Microsoft.VisualBasic'
    PRINT 'Imports StringFunctionsClass'
    PRINT 'Imports System.Data'
    PRINT 'Imports System.Data.SqlClient'
    PRINT 'Imports System.IO'
    PRINT 'Imports UPDB'
    PRINT ''
    PRINT 'Public Class  ' + @CLASS_NAME
    PRINT 'Inherits UPDB.UPDB_BaseDataClass'

	

    PRINT '''--'''
    PRINT '''--AUTO-GENERATED WITH SPROC Z:\a_Shared\ASP NET Propert Creator for Class Version VII.sql'''
    PRINT '''--'''
    PRINT ''
    IF @STRING_ERROR_CHECKING = '1'
        PRINT 'Dim oStringCleaner As New StringFunctionsClass'
    PRINT ''

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '#Region "COMMENTS_GENERAL_INFO"
		''<author></author>
		''<datecreated></datecreated>
		''<filename></filename>
		''<appname></appname>
		''<db></db>
		''<server></server>
		''<calledby name="" type="" />
		''<calls name="" type="" />
		''<summary></summary>		
		''<change by="" date=""></change>
		''<sample>
		''	<![cdata[ write sample code here: ]]>
		''</sample>
		#End Region
	'
    END
END

PRINT ''
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************START CREATE PROPERTIES****************************************************'
IF @CREATE_PROPERTIES = 1
    OR @CREATE_ENTIRE_CLASS = 1
BEGIN

    PRINT '	
 Dim _DATA_FOUND As String
    Public Property DATA_FOUND() As String
        Get
            ''Return the value of the variable''
            Return _UICID
        End Get
        ''Set the value of the variable''
        Set(ByVal sVal As String)
            If Not sVal Is DBNull.Value And Len(Trim(sVal)) > 0 Then
                _DATA_FOUND = oStringCleaner.MustBeProperCharacters(sVal, 1)
            Else
                _DATA_FOUND = "N"
            End If
        End Set
    End Property

'

    SET NOCOUNT ON
    DECLARE MyCursor INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [STRING_CHECKING]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursor
	
    FETCH NEXT FROM MyCursor INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@STRING_CHECKING
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN

            PRINT 'Dim _' + @COLUMN_NAME + ' AS ' + @VBNET_TYPE
            IF @CREATE_COMMENT_REGIONS = 1
            BEGIN
                PRINT '#Region "COMMENTS_PROPERTY"
			    ''<name>' + @COLUMN_NAME + '</name>
			    ''<returns></returns>
			    ''<private_name type=""></private_name>
			    ''<summary>Property name mirrors column name in the Entity Table</summary>	
			    #End Region'		
            END
            PRINT 'Public Property ' + @COLUMN_NAME + '() AS ' + @VBNET_TYPE
            PRINT '    Get '
            PRINT '         ''' + 'Return the value of the variable' + ''''
            PRINT '         Return _' + @COLUMN_NAME
            PRINT '    End Get '
            PRINT '    ''' + 'Set the value of the variable' + ''''
			
            PRINT '    Set(ByVal sVal As ' + @VBNET_TYPE + ') '
	
            IF @VBNET_TYPE = 'DATE'
            BEGIN
                PRINT '        If sVal > "01-JAN-1900" Then'
                PRINT '             _' + @COLUMN_NAME + '= sVal'
                PRINT '        Else'
                PRINT '             _' + @COLUMN_NAME + '= "01-JAN-1900"'
            END
            ELSE
                IF @VBNET_TYPE = 'STRING'
                    IF @STRING_CHECKING = '1'
                    BEGIN
                        PRINT '        If Not sVal Is DBNull.Value And Len(Trim(sVal)) > 0 Then'
                        PRINT '             _' + @COLUMN_NAME + '= oStringCleaner.MustBeProperCharacters(sVal,' + CONVERT(VARCHAR,@LEN) + ')'
                        PRINT '        Else'
                        PRINT '             _' + @COLUMN_NAME + '= ""'
                    END
                    ELSE
                    BEGIN
                        PRINT '        If Not sVal Is DBNull.Value And Len(Trim(sVal)) > 0 Then'
                        PRINT '             _' + @COLUMN_NAME + '= Trim(Left(sVal,' + CONVERT(VARCHAR,@LEN) + '))'
                        PRINT '        Else'
                        PRINT '             _' + @COLUMN_NAME + '= ""'
                    END
                ELSE
                    IF @VBNET_TYPE = 'INTEGER'
                    BEGIN
                        PRINT '        If sVal > 0 Then'
                        PRINT '             _' + @COLUMN_NAME + '= sVal'
                        PRINT '        Else'
                        PRINT '             _' + @COLUMN_NAME + '= 0'
                    END
                    ELSE
                        IF @VBNET_TYPE = 'LONG'
                        BEGIN
                            PRINT '        If sVal > 0 Then'
                            PRINT '             _' + @COLUMN_NAME + '= sVal'
                            PRINT '        Else'
                            PRINT '             _' + @COLUMN_NAME + '= 0'
                        END
                        ELSE
                        BEGIN
                            PRINT '        If Not sVal Is DBNull.Value And Len(Trim(sVal)) > 0 Then'
                            PRINT '             _' + @COLUMN_NAME + '= Trim(Left(sVal,' + CONVERT(VARCHAR,@LEN) + '))'
                            PRINT '        Else'
                            PRINT '             _' + @COLUMN_NAME + '= ""'
                        END
            PRINT '        End If'
            PRINT '    End Set'
            PRINT 'End Property'
        END
        FETCH NEXT FROM MyCursor INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@STRING_CHECKING
    END
    exitsub:
    DEALLOCATE MyCursor
    SET NOCOUNT OFF
END
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************END CREATE PROPERTIES****************************************************'
PRINT ''
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************START CREATE PROPERTIES INITIALIZATION CODE******************************'
IF @CREATE_INITIALIZATION = 1
    OR @CREATE_ENTIRE_CLASS = 1
BEGIN

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '#Region "COMMENTS_METHOD"
	'' <name>PopulateClassData</name>
    	'' <returns>VOID</returns>--required for functions only
    	'' <summary>
    	''	This public-facing Sub can be called from an aspx.vb page in a Web applicaiton.
    	''	The PopulateClassData sub call the private sub PopulateData to accomlish this
    	'' </summary>
    	'' <param name="ParameterName" type="String">
    	''	  <ref_type>ByValue</ref_type>
    	''	  <required>Yes</required>
    	''	  <summary>
    	''		The parameter passed to PopulateClassData
	''		is a value that uniquely identifies the 
	''		entity in the data table
    	''	   </summary>
    	'' </param>
	#End Region'		
    END

    PRINT ' Public Sub PopulateClassData(ByVal ' + @ENTITY_ID_VARIABLE + ' As ' + @ENTITY_ID_VARIABLE_TYPE + ')'
    PRINT '        PopulateData(' + @ENTITY_ID_VARIABLE + ')'
    PRINT ' End Sub'

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '#Region "COMMENTS_METHOD"
	'' <name>PopulateData</name>
    	'' <returns>VOID</returns>--required for functions only
    	'' <summary>
    	''	PopulateData retrieves data from the entity table and populates
    	''	the properties IN the instantiated class.
    	''	An important concept used in this sub is that
    	''	SqlCommand object AND the SqlCommand objects paramaters
    	''	are defined within Sub PopulateData. The SqlCommand object is then
    	''	sent to a generic Data Base handler (another class that manages 
    	''	connecting to AND retrieving data FROM the SQL server) FUNCTION called
    	''	ExecuteSqlCommand that takes OVER the task OF making the CONNECTION TO the
    	''	DATABASE AND returing a record SET TO Sub PopulateData 
    	''</summary>
    	'' <param name="ParameterName" type="String">
    	''	  <ref_type>ByValue</ref_type>
    	''	  <required>Yes</required>
    	''	  <summary>
   	''		The parameter passed to PopulateClassData
	''		is a value that uniquely identifies the 
	''		entity in the data table
    	''	  </summary>
    	'' </param>
	#End Region'		
    END

    PRINT ' Private Sub PopulateData(ByVal ' + @ENTITY_ID_VARIABLE + ' As ' + @ENTITY_ID_VARIABLE_TYPE + ')'
    PRINT ''
	
    PRINT '	Dim ' + @DATABASE_HANDLER_INSTANTIATED_INSTANCE_NAME + ' As New DataBaseHandler'
    PRINT '	Dim oReader As SqlDataReader = Nothing'
    PRINT '	Dim oSqlCom As New SqlCommand'
    PRINT ''
    PRINT 'Try'
    PRINT ' oSqlCom.CommandType = CommandType.StoredProcedure'            
    PRINT ' oSqlCom.CommandText = "' + @SPROC_FOR_GETTNG_ENTITY_DATA + '"'    
    PRINT '''--Set proper values for the SQL SPROC input parameter here!'
    PRINT '	oSqlCom.Parameters.Add("@' + @ENTITY_ID_VARIABLE + '", SqlDbType.VarChar, 0).Value = ' + @ENTITY_ID_VARIABLE + '.ToString'
    PRINT 'oReader = ' + @DATABASE_HANDLER_INSTANTIATED_INSTANCE_NAME + '.' + @DATABASE_UPDATE_HANDLER_PROCEDURE + '(oSqlCom)''--(oSqlCom, [Optional] ServerName,[Optional] Databasename)'
    PRINT ''
    PRINT '	Me.DATA_FOUND = "N"'
    PRINT '	If (oReader.HasRows And oReader.Read) Then'
    PRINT '		Me.DATA_FOUND = "Y"'

    SET NOCOUNT ON
    DECLARE MyCursorII INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorII
    FETCH NEXT FROM MyCursorII INTO @COLUMN_NAME,@LEN,@VBNET_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @VBNET_TYPE = 'STRING'
            BEGIN
                PRINT '		Me.' + @COLUMN_NAME + '=oReader("' + @COLUMN_NAME + '").ToString'
            END	
            ELSE
            BEGIN
                PRINT '		Me.' + @COLUMN_NAME + '=oReader("' + @COLUMN_NAME + '")'
            END
        END
        FETCH NEXT FROM MyCursorII INTO @COLUMN_NAME,@LEN,@VBNET_TYPE
    END
    exitsubII:
    DEALLOCATE MyCursorII
	
    PRINT ''
    PRINT '		Else'

    PRINT '	'
    PRINT '		Me.DATA_FOUND = "N"'
                
    DECLARE MyCursorInit INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            UPPER([SQL_TYPE])
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorInit
    FETCH NEXT FROM MyCursorInit INTO @COLUMN_NAME,@LEN,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN

            IF @SQL_TYPE IN ('VARCHAR','CHAR','NVARCHAR')
                PRINT '		ME.' + @COLUMN_NAME + '= ' + '"' + '"'
            ELSE
                IF @SQL_TYPE IN ('INT','TINYINT','NUMERIC','REAL','SMALLINT')
                    PRINT '		ME.' + @COLUMN_NAME + '= 0'
                ELSE
                    IF @SQL_TYPE IN ('DATETIME')
                        PRINT '		ME.' + @COLUMN_NAME + '= "01-JAN-1900"'
                    ELSE
                        PRINT '	--' + @COLUMN_NAME + ' | ' + @SQL_TYPE 

        END
        FETCH NEXT FROM MyCursorInit INTO @COLUMN_NAME,@LEN,@SQL_TYPE
    END 
    DEALLOCATE MyCursorInit

    PRINT '	End If'
    PRINT ''
    PRINT ''
    PRINT ''
    PRINT '       Catch ex As System.Exception'
    PRINT '            Dim txtErrorString As New StringBuilder'
    PRINT '            With txtErrorString'
    PRINT '                .Append("Error Getting Service Data") '
    PRINT '                .Append(vbLf)'
    PRINT '                .Append(vbLf)'
    PRINT '            End With'
    PRINT ''
    PRINT '            Dim txtError As String = txtErrorString.ToString'
    PRINT '            Dim txtAdminMessage As String = ""'
    PRINT '            txtAdminMessage = ("Souce: " & HttpContext.Current.Request.RawUrl.ToString & vbLf)'
    PRINT '            txtAdminMessage = txtAdminMessage & ("Error In: " & HttpContext.Current.Request.Url.ToString & vbLf)'
    PRINT '            txtAdminMessage = txtAdminMessage & ("Error Message: " & ex.Message.ToString & vbLf)'
    PRINT '            txtAdminMessage = txtAdminMessage & ("Stack Trace: " & ex.StackTrace.ToString & vbLf)'
    PRINT '            HttpContext.Current.Server.Transfer("ErrorNotification.aspx?txtError=" & txtError & "&txtAdminMessage=" & txtAdminMessage)'
    PRINT '        End Try'
    PRINT 'End Sub'
END

IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************END CREATE PROPERTIES INITIALIZATION CODE****************************************************'
PRINT ''
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************START CREATE CALL PARAMATARIZED UPDATE PROCEDURE FROM CLASS**********************************'
IF @CREATE_CALL_PARAMATERIZEDUPDATE_PROCEDURE_FROM_CLASS = 1
    OR @CREATE_ENTIRE_CLASS = 1
BEGIN	

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '#Region "COMMENTS_METHOD"
	'' <name>UpdateClassTables</name>
    	'' <returns>VOID</returns>--required for functions only
    	'' <summary>
    	''	This public-facing sub is called from the aspx.vb page. 
    	''      UpdateClassTables calls UpdateClassTables which updates 
    	''	data in the entity table
    	''</summary>    	
    	'' <param name="oData" type="String">
    	''	  <ref_type>ByValue</ref_type>
    	''	  <required>Yes</required>
    	''	  <summary>An instance of the entity class</summary>
    	'' </param>
	#End Region'		
    END

    PRINT ' Public Sub UpdateClassTables(ByVal oData As ' + @CLASS_NAME + ')'
    PRINT '        UpdateTables(oData)'
    PRINT ' End Sub'

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '#Region "COMMENTS_METHOD"
	'' <name>MethodName</name>
    	'' <returns>VOID</returns>--required for functions only
    	'' <summary>
    	''      UpdateClassTables which updates data in the entity table
    	''	An important concept used in this sub is that
    	''	SqlCommand object AND the SqlCommand objects paramaters
    	''	are defined within Sub UpdateClassTables. The SqlCommand object is then
    	''	sent to a generic Data Base handler (another class that manages 
    	''	connecting to AND retrieving data FROM the SQL server) FUNCTION called
    	''	ExecuteSqlCommand that takes OVER the task OF making the CONNECTION TO the
    	''	DATABASE AND performing the required action 
    	''</summary>    	
    	'' <param name="oData" type="String">
    	''	  <ref_type>ByValue</ref_type>
    	''	  <required>Yes</required>
    	''	  <summary>An instance of the entity class</summary>
    	'' </param>
	#End Region'		
    END

    PRINT ' Private Sub UpdateTables(ByVal oData As ' + @CLASS_NAME + ')'
    PRINT ''


    PRINT '	Dim ' + @DATABASE_HANDLER_INSTANTIATED_INSTANCE_NAME + ' As New ' + @DATABASE_UPDATE_HANDLER_CLASS_NAME
    PRINT '	''--oReader is used to return a process result message from the update procedure'
    PRINT '	Dim oReader As SqlDataReader = Nothing'
    PRINT ''
    PRINT '	Dim oSqlCom As New SqlCommand'
    PRINT ' oSqlCom.CommandType = CommandType.StoredProcedure'            
    PRINT ' oSqlCom.CommandText = "' + @SPROC_FOR_UPDATES + '"'    

    PRINT ''
    PRINT 'Try'

    DECLARE MyCursorIV INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorIV
    FETCH NEXT FROM MyCursorIV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @VBNET_TYPE = 'String'
            BEGIN
                PRINT '	oSqlCom.Parameters.Add("@' + @COLUMN_NAME + '", SqlDbType.VarChar, ' + CONVERT(VARCHAR,@LEN) + ').Value = oData.' + @COLUMN_NAME + '.ToString'
            END
            ELSE
            BEGIN
                PRINT '	oSqlCom.Parameters.Add("@' + @COLUMN_NAME + '", SqlDbType.' + @SQL_TYPE + ').Value = oData.' + @COLUMN_NAME 
            END
        END
        FETCH NEXT FROM MyCursorIV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    END 
    DEALLOCATE MyCursorIV
	

    PRINT '			oReader = ' + @DATABASE_HANDLER_INSTANTIATED_INSTANCE_NAME + '.' + @DATABASE_UPDATE_HANDLER_PROCEDURE + '(oSqlCom)''--(oSqlCom, [Optional] ServerName,[Optional] Databasename)'
	
    PRINT ''
    PRINT '''*************************************************************************************************************
            ''***Put the following two functions in your database handler class***
            ''Private Function DbConnection(Optional ByVal txtOptionalServer As String = "", _
            ''                                 Optional ByVal txtOptionalDatabase As String = "") As SqlConnection

            ''    Dim MyConnection As New SqlConnection
            ''    Dim txtServer As String = ""
            ''   Dim txtDatabase As String = ""

            ''    If txtOptionalServer = "" Then
            ''' + '--Set in Web.Config in  <appSettings> section; e.g., <add key="DEFAULT_SERVER" value="UPDB_DEV_SQL"/>
            ''       txtServer = ConfigurationManager.AppSettings("DEFAULT_SERVER") 

            ''    Else
            ''        txtServer = txtOptionalServer
            ''    End If

            ''    If txtOptionalDatabase = "" Then
            ''' + '--Set in Web.Config in  <appSettings> section; e.g., <add key="DEFAULT_DB" value="PORTAL"/>
            ''        txtDatabase = ConfigurationManager.AppSettings("DEFAULT_DB") --SET IN WEB CONFIG
            ''    Else
            ''        txtDatabase = txtOptionalDatabase
            ''   End If

            ''    Try
            ''        MyConnection.ConnectionString = ("server=" & txtServer & ";database=" & txtDatabase & ";Trusted_Connection=yes; Pooling=false; Connection Timeout=45")

            ''    Catch ex As SystemException
            ''        Throw New Exception(ex.Message.ToString & " | " & " ERROR OCCURRED AT LINE: " & ex.StackTrace.ToString)
            ''    End Try

            ''    Return MyConnection

            ''End Function

            ''Public Function ExecuteSqlCommand(ByVal oSqlCom As SqlCommand, _
            ''                                        Optional ByVal strServer As String = "", _
            ''                                        Optional ByVal strDatabase As String = "") As SqlDataReader

            ''' + '--Paramaters for the receiving SPROC are set in the calling function/class and passed as part of the SqlCommand Object
            ''    Dim oSqlConn As New SqlConnection
            ''    Dim oReader As SqlDataReader = Nothing

            ''    Try
            ''        oSqlConn = DbConnection(strServer, strDatabase)
            ''        oSqlCom.Connection = oSqlConn

            ''        oSqlConn.Open()
            ''        oReader = oSqlCom.ExecuteReader(System.Data.CommandBehavior.CloseConnection)


            ''    Catch ex As SystemException
            ''        Throw New Exception(ex.Message.ToString & " | " & " ERROR OCCURRED AT LINE: " & ex.StackTrace.ToString)
            ''    End Try
            ''    Return oReader

            ''End Function
            ''*************************************************************************************************************'
            

    PRINT ''
    PRINT '       Catch ex As System.Exception'
    PRINT '            Dim txtErrorString As New StringBuilder'
    PRINT '            With txtErrorString'
    PRINT '                .Append("Error Updating Servcie Data") '
    PRINT '                .Append(vbLf)'
    PRINT '                .Append(vbLf)'
    PRINT '            End With'
    PRINT ''
    PRINT '            Dim txtError As String = txtErrorString.ToString'
    PRINT '            Dim txtAdminMessage As String = ""'
    PRINT '            txtAdminMessage = ("Souce: " & HttpContext.Current.Request.RawUrl.ToString & vbLf)'
    PRINT '            txtAdminMessage = txtAdminMessage & ("Error In: " & HttpContext.Current.Request.Url.ToString & vbLf)'
    PRINT '            txtAdminMessage = txtAdminMessage & ("Error Message: " & ex.Message.ToString & vbLf)'
    PRINT '            txtAdminMessage = txtAdminMessage & ("Stack Trace: " & ex.StackTrace.ToString & vbLf)'
    PRINT '            HttpContext.Current.Server.Transfer("ErrorNotification.aspx?txtError=" & txtError & "&txtAdminMessage=" & txtAdminMessage)'
    PRINT '        End Try'
    PRINT 'End Sub'

END
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************END CREATE CALL PARAMATARIZED UPDATE PROCEDURE FROM CLASS**********************************'
PRINT ''
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************START CREATE PARAMATERIZED UPDATE PROCEDURE IN DATABASE HANDLER****************************'
IF @CREATE_PARAMATERIZEDUPDATE_PROCEDURE = 1
BEGIN

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '#Region "COMMENTS_METHOD"
	'' <name>MethodName</name>
    	'' <returns>VOID</returns>--required for functions only
    	'' <summary>Description</summary>
    	'' <param name="ParameterName" type="String">
    	''	  <ref_type>ByValue</ref_type>
    	''	  <required>Yes</required>
    	''	  <summary>ParameterDecription</summary>
    	'' </param>
	#End Region'		
    END

    PRINT '    Public Function ' + @DATABASE_UPDATE_HANDLER_PROCEDURE + '(ByVal inSqlCom As SqlCommand) As SqlDataReader'
    PRINT '	'	
    PRINT '	'		
    PRINT '			''--Paramaters for the receiving SPROC are set in the calling class and passed as a value to this procedure'
    PRINT '			Dim sqlConn As New SqlConnection'
    PRINT '		        Dim oReader As SqlDataReader = Nothing'
    PRINT '		        Dim sqlCom As SqlCommand = inSqlCom'
    PRINT '	'	
    PRINT '		        Try'
    PRINT '		            sqlConn = CrConnection("", "PORTAL")'
    PRINT '		            sqlCom.CommandType = CommandType.StoredProcedure'
    PRINT '		            sqlCom.CommandText = "' + @SPROC_FOR_UPDATES + '"'
    PRINT '		            sqlCom.Connection = sqlConn'
    PRINT '	'	
    PRINT '		            sqlConn.Open()'
    PRINT '		            oReader = sqlCom.ExecuteReader(System.Data.CommandBehavior.CloseConnection)'
    PRINT ''
    PRINT ''
    PRINT '		        Catch ex As Exception'
    PRINT '		            Dim txtError As String = ""'
    PRINT '		            Dim txtAdminMessage As String'
    PRINT '		            txtAdminMessage = ("Souce: " & HttpContext.Current.Request.RawUrl.ToString & vbLf)'
    PRINT '		            txtAdminMessage = txtAdminMessage & ("Error In: " & HttpContext.Current.Request.Url.ToString & vbLf)'
    PRINT '		            txtAdminMessage = txtAdminMessage & ("Error Message: " & ex.Message.ToString & vbLf)'
    PRINT '		            txtAdminMessage = txtAdminMessage & ("Stack Trace: " & ex.StackTrace.ToString & vbLf)'
    PRINT '		            HttpContext.Current.Server.Transfer("ErrorNotification.aspx?txtError=" & txtError & "&txtAdminMessage=" & txtAdminMessage)'
    PRINT '		        End Try'
    PRINT '		       Return oReader'
    PRINT '	'	
    PRINT 'End Function'
END	
IF @CREATE_ENTIRE_CLASS = 0
    PRINT '****************************************************END CREATE PARAMATERIZED UPDATE PROCEDURE IN DATABASE HANDLER****************************'
PRINT ''

IF @CREATE_SPROC_FRAMEWORK = 1
BEGIN
    PRINT '''**************************START CREATE FRAMEWORK FOR SQL STORED PROCEDURE TO UPDATE TABLE(S)*************************************************'

    PRINT 'SET QUOTED_IDENTIFIER ON '
    PRINT 'GO'
	
    PRINT 'SET ANSI_NULLS ON '
    PRINT 'GO'

    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '
-- =============================================
-- Author:		' + SUSER_SNAME() + '
-- Create date: ' + CONVERT(VARCHAR,GETDATE(),111) + '
-- Description:	Shreds an XML document to relational table intended to Create, Update, or Delete rows in ' + @ENTITY_TABLE_OR_VIEW_NAME + '
-- After running do the following: 
-- INSERT fixes:
-- 1. Replace TABLE with the ID of the target table in entire procedure
-- 2. Remove comma after the last column in the CREATE Table statement
-- 3. Remove <sql_system_user> from last_update_date BY_WHO as appropriate from test data/ SELECT in INSERT. 
-- 4. Remove all computed columns from the CREATE/INSERT/SELECT statements and test XML
-- 5. Remove the TABLE_ID Column from the CREATE/INSERT/SELECT statements
-- 6. Set correct dates/non nulls in test data. Remove all other bugs I wasn''t smart enough TO find
-- UPDATE fixes:
-- 1. Replace TABLE_ID with ID of target table
-- 2. Remove comma after the last column in the SET column list.
-- 3. PROD.LAST_UDPATE_DATE = GETDATE(), SQL_SYSTEM_USER = SYSTEM_USER, BY_WHO= @BY_WHO
-- 4. Remove the update to the TABLE_ID
-- DELETE fixes:
-- ============================================='
    END    

    PRINT 'CREATE  PROCEDURE ' + @SPROC_FOR_UPDATES 
    PRINT '		
	 @XML_DATA XML = '''
	 PRINT '    <rows>
	<category>CLASS</category>
	<http_referer>HTTP_REFERER</http_referer>
	<url>URL</url>
	<by_who>X_CAC_X</by_who>
		<row>
			<action>INSERT</action>'
	
    DECLARE MyCursorV INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorV
    FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
                PRINT '			<' + LOWER(@COLUMN_NAME) + '>' + '</' + LOWER(@COLUMN_NAME) + '>'
        END
        FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    END 
    DEALLOCATE MyCursorV
    PRINT '		</row>'
    PRINT '	</rows>'
    PRINT ''''
    PRINT 'AS'
    PRINT 'BEGIN --PROC'
    PRINT '	--SET NOCOUNT ON added to prevent extra result sets from interfering with SELECT statements.'
    PRINT '	SET NOCOUNT ON'
    PRINT '    BEGIN TRY	--Try procedure as a whole, if anything fails, capture it and rollback all actions'
	PRINT '		BEGIN TRANSACTION	--https://msdn.microsoft.com/en-us/library/ms178592.aspx'
	PRINT '		--Set "Per Transaction" Variables from the root node level'
	PRINT '		--Default error variables here'
    PRINT '        DECLARE @HTTP_REFERER NVARCHAR(300)'
    PRINT '        SET @HTTP_REFERER = @XML_DATA.value(''(/rows/http_referer)[1]'',''NVARCHAR(300)'')'
    PRINT '        DECLARE @URL NVARCHAR(300)'
    PRINT '        SET @URL = @XML_DATA.value(''(/rows/url)[1]'',''NVARCHAR(300)'')'  
    PRINT '        DECLARE @BY_WHO NVARCHAR(50)'
    PRINT '        SET @BY_WHO = @XML_DATA.value(''(/rows/by_who)[1]'',''NVARCHAR(50)'')'  
    PRINT ''
    PRINT '		--Custom "Per Transaction Variables" here'
    PRINT ''
    PRINT '		--Build SQL Command to shred XML Document to relational table '
    PRINT '        DECLARE @SQL NVARCHAR(MAX) = '''''
    PRINT '        DECLARE @Col NVARCHAR(MAX) = '', T.N.value(''''[COLNAME][1]'''', ''''varchar(100)'''') as [COLNAME]''' 
    PRINT '        SELECT  @SQL = @SQL + REPLACE(@Col,''[COLNAME]'',T.N.value(''local-name(.)'',''sysname''))'
    PRINT '        FROM    @XML_DATA.nodes(''/rows/row[1]/*'') AS T (N)'
    PRINT '        SET @SQL = ''select '' + STUFF(@SQL,1,2,'''') + '' from @XML_DATA.nodes(''''/rows/row'''') as T(N)''' 
	PRINT ''
	PRINT '		--Create a temp table base on the Schema of target table'
	PRINT '		CREATE TABLE #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','')  +'('
	PRINT ' ACTION NVARCHAR(10) NULL,'
    DECLARE MyCursorV INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorV
    FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @VBNET_TYPE = 'String'
            BEGIN
                PRINT '			' + @COLUMN_NAME + ' NVARCHAR(' + CONVERT(VARCHAR,@LEN) + '),'
            END
            ELSE
            BEGIN
                PRINT '			' + @COLUMN_NAME + ' ' + UPPER(@SQL_TYPE) + ','
            END
        END
        FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    END 
    DEALLOCATE MyCursorV
    PRINT '		)'
    PRINT ''
    PRINT'			--Shred XML Document to temporary table. This command will expect to put whatever is in the <row> level nodes into the table'
    PRINT'			--"Per transaction" columns cannot exist yet, otherwise you will get a mismatch on insert values'
    PRINT'            INSERT #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','')
    PRINT'                    EXEC sp_executesql @SQL,N''@XML_DATA xml'',@XML_DATA'
    PRINT''
    PRINT'			/*If needed add "Per Transaction" Columns here
    		--Add the CATEGORY "Per Transaction COLUMN
            ALTER TABLE [#tblCOMMENTS]
            ADD CATEGORY NVARCHAR(16)            
			--Update temp table to reflect the CATEGORY Passed
            UPDATE  [#tblCOMMENTS]
            SET     [CATEGORY] = @CATEGORY*/'            
	PRINT'			--Check to see if there are any insert actions in the temporary table. If TABLE_ID = 0, it is an INSERT'            
	PRINT'            IF EXISTS ( SELECT  TABLE_ID FROM    #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' WHERE TABLE_ID = 0 )'
	PRINT'            BEGIN --INSERT'
	PRINT'            INSERT  INTO ' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','')  +'('
	DECLARE MyCursorV INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorV
    FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN

            BEGIN
                PRINT '				' + @COLUMN_NAME + ','
            END

        END
        FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    END 
    DEALLOCATE MyCursorV
    PRINT'			BY_WHO,'
	PRINT'			SQL_SYSTEM_USER,'
	PRINT'			LAST_UPDATE_DATE'
    PRINT '		)'
    PRINT'			SELECT '
DECLARE MyCursorV INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorV
    FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN

            BEGIN
                PRINT '				' + @COLUMN_NAME + ','
            END

        END
        FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    END 
    DEALLOCATE MyCursorV  
    PRINT'				@BY_WHO,'  
    PRINT'				SYSTEM_USER,'
    PRINT'				GETDATE()'
    PRINT'				FROM #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' AS TEMP'
    PRINT'				WHERE TEMP.TABLE_ID = 0'
    PRINT'			END --INSERT'    
    PRINT''
    PRINT'			--Since the passed TABLE_ID is NOT 0, it must be an update or a delete action '
	PRINT'			IF EXISTS ( SELECT  TABLE_ID FROM    #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' WHERE ACTION=''UPDATE'' )'
	PRINT'			BEGIN --Update'
	PRINT'			Print ''UPDATING'''      
	PRINT'			UPDATE PROD'
	PRINT'			SET '
	DECLARE MyCursorV INSENSITIVE CURSOR
    FOR
    SELECT DISTINCT
            [COLUMN_NAME],
            [LENGTH],
            [VBNET_TYPE],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY COLUMN_NAME
    OPEN MyCursorV
    FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
                PRINT '			PROD.' + UPPER(@COLUMN_NAME) + ' = ' + 'TEMP.' + UPPER(@COLUMN_NAME) + ','
        END
        FETCH NEXT FROM MyCursorV INTO @COLUMN_NAME,@LEN,@VBNET_TYPE,@SQL_TYPE
    END
    DEALLOCATE MyCursorV  
    PRINT'		FROM ' + '#' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' AS TEMP'
    PRINT'			INNER JOIN ' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' AS PROD ON PROD.TABLE_ID = TEMP.TABLE_ID AND TEMP.ACTION = ''UPDATE'''
    PRINT'		END'
	PRINT'		      --Check to see if there are any insert actions in the temporary table. If TABLE_ID = 0, it is an INSERT'            
	PRINT'            IF EXISTS ( SELECT  TABLE_ID FROM    #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' WHERE ACTION = ''DELETE''' +')'
	PRINT'				BEGIN --DELETE'
	PRINT'					PRINT''DELETING'''
	PRINT'					DELETE FROM ' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','')  
	PRINT'					WHERE	TABLE_id IN (SELECT TABLE_ID FROM #' + REPLACE(@ENTITY_TABLE_OR_VIEW_NAME,'''','') + ' WHERE ACTION = ''DELETE''' +')'
	PRINT'				END'					
PRINT' COMMIT TRANSACTION'
PRINT' END TRY'
PRINT'BEGIN CATCH'
PRINT'ROLLBACK TRANSACTION
             --Declare variables to hold RAISERROR variables
            DECLARE @ERROR_MESSAGE NVARCHAR(4000);
            DECLARE @ERROR_SEVERITY INT;
            DECLARE @ERROR_STATE INT;
            --Set variables
            SELECT  @ERROR_MESSAGE = ERROR_MESSAGE(),
                    @ERROR_SEVERITY = ERROR_SEVERITY(),
                    @ERROR_STATE = ERROR_STATE();
			--RAISERROR only generates errors with state from 1 through 127. Because the Database Engine may raise errors with state 0, 
			--we recommend that you check the error state returned by ERROR_STATE before passing it as a value to the state parameter of 
			--RAISERROR. https://msdn.microsoft.com/en-us/library/ms178592.aspx                    
            IF @ERROR_STATE = 0
            BEGIN
                SELECT  ''Error State 0; '' + @ERROR_MESSAGE,
                        @ERROR_SEVERITY,
                        @ERROR_STATE		
                EXEC usp_SYS_SQL_ERROR_HANDLER @HTTP_REFERER,@URL,@BY_WHO  --Capture this in dbo.tbl_DBA_SQL_ERRORS
                SELECT  ''Error occurred in SP '' + ERROR_PROCEDURE() + '';entire transaction ROLLED BACK'' AS ERROR
                RETURN 1 --Return 1 to notify failure
            END
			--If the state is not zero, throw an error.
            ELSE
				--Let''s throw an error if the state is != 0
                RAISERROR (@ERROR_MESSAGE,	-- Message text.
               @ERROR_SEVERITY,			-- Severity.
               @ERROR_STATE				-- State.
               );      
            EXEC usp_SYS_SQL_ERROR_HANDLER @HTTP_REFERER,@URL,@BY_WHO --Capture this in dbo.tbl_DBA_SQL_ERRORS
            SELECT  ''Error occurred in SP '' + ERROR_PROCEDURE() + '';entire transaction ROLLED BACK'' AS ERROR
            RETURN 1 --Return 1 to notify failure'''
PRINT'END CATCH'
PRINT'END --PROC'
    DECLARE MyCursorVI INSENSITIVE CURSOR
    FOR
    SELECT  DISTINCT
            [TABLE_NAME],
            [COLUMN_NAME]
    FROM    zzTemp_ClassCreator
    ORDER BY [TABLE_NAME],
            [COLUMN_NAME]
    OPEN MyCursorVI

    FETCH NEXT FROM MyCursorVI INTO @TABLE_NAME,@COLUMN_NAME
    FETCH NEXT FROM MyCursorVI INTO @TABLE_NAME2,@COLUMN_NAME2

    PRINT 'DECLARE @TYPE_ACTION VARCHAR(25)'
    PRINT 'SET @TYPE_ACTION=''UPDATE'''
    PRINT ''
    PRINT ''
    PRINT 'IF @TYPE_ACTION=''UPDATE'''
    PRINT 'BEGIN'
    PRINT '	UPDATE ' + @TABLE_NAME
    PRINT '	SET ' 
    PRINT '		' + @COLUMN_NAME + ' = @' + @COLUMN_NAME + ','


    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @TABLE_NAME = @TABLE_NAME2
            BEGIN
                PRINT '		' + @COLUMN_NAME2 + ' = @' + @COLUMN_NAME2 + ','
            END
            ELSE
            BEGIN
                PRINT '	UPDATE ' + @TABLE_NAME2
                PRINT '	SET ' 
                PRINT '		' + @COLUMN_NAME2 + ' = @' + @COLUMN_NAME2 + ','
            END
        END
        SET @TABLE_NAME = @TABLE_NAME2
        SET @COLUMN_NAME = @COLUMN_NAME2
	
        FETCH NEXT FROM MyCursorVI INTO @TABLE_NAME2,@COLUMN_NAME2
    END 
    DEALLOCATE MyCursorVI

    PRINT 'END'
    PRINT ' '
    PRINT ' '
    PRINT 'IF @TYPE_ACTION=''INSERT'''
    PRINT 'BEGIN'
    DECLARE MyCursorVII INSENSITIVE CURSOR
    FOR
    SELECT  DISTINCT
            [TABLE_NAME],
            [COLUMN_NAME]
    FROM    zzTemp_ClassCreator
    ORDER BY [TABLE_NAME],
            [COLUMN_NAME]
    OPEN MyCursorVII

    FETCH NEXT FROM MyCursorVII INTO @TABLE_NAME,@COLUMN_NAME
    FETCH NEXT FROM MyCursorVII INTO @TABLE_NAME2,@COLUMN_NAME2

    PRINT '	INSERT INTO ' + @TABLE_NAME
    PRINT '	('	 
    PRINT '	' + @COLUMN_NAME + ','

    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @TABLE_NAME = @TABLE_NAME2
            BEGIN
                PRINT '	' + @COLUMN_NAME2 + ','
            END
            ELSE
            BEGIN
                PRINT '	)'
                PRINT ''
                PRINT ''
                PRINT '	INSERT INTO ' + @TABLE_NAME2
                PRINT '('	 
                PRINT '' + @COLUMN_NAME2 + ','
            END
        END
        SET @TABLE_NAME = @TABLE_NAME2
        SET @COLUMN_NAME = @COLUMN_NAME2
	
        FETCH NEXT FROM MyCursorVII INTO @TABLE_NAME2,@COLUMN_NAME2
    END 
    DEALLOCATE MyCursorVII
    PRINT '	)'	


    DECLARE MyCursorVIII INSENSITIVE CURSOR
    FOR
    SELECT  DISTINCT
            [TABLE_NAME],
            [COLUMN_NAME]
    FROM    zzTemp_ClassCreator
    ORDER BY [TABLE_NAME],
            [COLUMN_NAME]

    OPEN MyCursorVIII

    FETCH NEXT FROM MyCursorVIII INTO @TABLE_NAME,@COLUMN_NAME
    FETCH NEXT FROM MyCursorVIII INTO @TABLE_NAME2,@COLUMN_NAME2

    PRINT 'SELECT'
    PRINT '	@' + @COLUMN_NAME + ','

    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @TABLE_NAME = @TABLE_NAME2
            BEGIN
                PRINT '	@' + @COLUMN_NAME2 + ','
            END
            ELSE
            BEGIN
                PRINT ''
                PRINT ''
                PRINT 'SELECT'
                PRINT '	@' + @COLUMN_NAME2 + ','
            END
        END
        SET @TABLE_NAME = @TABLE_NAME2
        SET @COLUMN_NAME = @COLUMN_NAME2
	
        FETCH NEXT FROM MyCursorVIII INTO @TABLE_NAME2,@COLUMN_NAME2
    END 
    DEALLOCATE MyCursorVIII
    PRINT ')'	

    PRINT '--END CODE HERE--'
    PRINT ''
    PRINT ''
	
	
    PRINT 'GRANT  EXECUTE  ON [dbo].[EAS_GET_POC]  TO [WEB_APPUSER]'
    PRINT 'GO'
    PRINT ''
    PRINT 'SET QUOTED_IDENTIFIER OFF' 
    PRINT 'GO'
    PRINT 'SET ANSI_NULLS OFF' 
    PRINT 'GO'

    PRINT ''
    PRINT ''
    PRINT ''
    PRINT 'SET QUOTED_IDENTIFIER ON '
    PRINT 'GO'
    PRINT 'SET ANSI_NULLS ON '
    PRINT 'GO'
    PRINT 'CREATE  PROCEDURE ' + @SPROC_FOR_GETTNG_ENTITY_DATA
    PRINT '	(@' + @ENTITY_ID_VARIABLE + '(VARCHAR(1))'

    PRINT ''
    PRINT 'AS'
    
    IF @CREATE_COMMENT_REGIONS = 1
    BEGIN
        PRINT '
	/*	
	<GENERAL_INFO>
		<AUTHOR>
		' + SUSER_SNAME() + '
		</AUTHOR>
		<DATECREATED>
		' + CONVERT(VARCHAR,GETDATE()) + '
		</DATECREATED>
		<FILENAME>
		' + @SPROC_FOR_GETTNG_ENTITY_DATA + '
		</FILENAME>
		<APPNAME>
		' + @CLASS_NAME + '	
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
	'
    END
    PRINT '--SELECT COLUMN DATA FOR SPROC READER'
    PRINT ''
    DECLARE MyCursorVIIII INSENSITIVE CURSOR
    FOR
    SELECT  DISTINCT
            [TABLE_NAME],
            [COLUMN_NAME],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY [TABLE_NAME],
            [COLUMN_NAME]

    OPEN MyCursorVIIII
    FETCH NEXT FROM MyCursorVIIII INTO @TABLE_NAME,@COLUMN_NAME,@SQL_TYPE
    PRINT 'IF (SELECT COUNT(*) FROM ' + @TABLE_NAME + ')>0'
    PRINT 'BEGIN'
    PRINT 'SELECT * FROM ' + @TABLE_NAME

    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
        BEGIN
            IF @SQL_TYPE IN ('VARCHAR','CHAR','NVARCHAR')
                PRINT '	ISNULL([' + @TABLE_NAME + '].[' + @COLUMN_NAME + '],'''')AS ' + @COLUMN_NAME + ','
            ELSE
                IF @SQL_TYPE IN ('INT','TINYINT','NUMERIC','REAL','SMALLINT')
                    PRINT '	ISNULL([' + @TABLE_NAME + '].[' + @COLUMN_NAME + '],0)AS ' + @COLUMN_NAME + ','
                ELSE
                    IF @SQL_TYPE IN ('DATETIME')
                        PRINT '	ISNULL([' + @TABLE_NAME + '].[' + @COLUMN_NAME + '],''01-JAN-1900'')AS ' + @COLUMN_NAME + ','
                    ELSE
                        PRINT '--' + @COLUMN_NAME + ' | ' + @SQL_TYPE 

        END
	
        FETCH NEXT FROM MyCursorVIIII INTO @TABLE_NAME,@COLUMN_NAME,@SQL_TYPE
    END 
    DEALLOCATE MyCursorVIIII
    PRINT 'WHERE [IDENTITY COLUMN] = @' + @ENTITY_ID_VARIABLE
    PRINT 'END'
    PRINT 'ELSE'
    PRINT 'BEGIN'
    PRINT ''
    PRINT '--USE TO RETURN A ZERO DATA SET IF THE ROW NOT IN SELECTED TABLE - SOMETIMES NEEDED FOR CLASS'
    PRINT ''

    DECLARE MyCursorX INSENSITIVE CURSOR
    FOR
    SELECT  DISTINCT
            [TABLE_NAME],
            [COLUMN_NAME],
            [SQL_TYPE]
    FROM    zzTemp_ClassCreator
    ORDER BY [TABLE_NAME],
            [COLUMN_NAME]

    OPEN MyCursorX

    FETCH NEXT FROM MyCursorX INTO @TABLE_NAME,@COLUMN_NAME,@SQL_TYPE
    PRINT '	SELECT'
    WHILE (@@fetch_Status <> -1)
    BEGIN
        IF (@@fetch_Status <> -2)
            IF @SQL_TYPE IN ('VARCHAR','CHAR','NVARCHAR')
                PRINT '	''''  AS ' + @COLUMN_NAME + ',' 
            ELSE
                IF @SQL_TYPE IN ('INT','TINYINT','NUMERIC','REAL','SMALLINT')
                    PRINT '	0  AS ' + @COLUMN_NAME + ','
                ELSE
                    IF @SQL_TYPE IN ('DATETIME')
                        PRINT '	''01-JAN-1900''  AS ' + @COLUMN_NAME + ','
                    ELSE
                        PRINT '--' + @COLUMN_NAME + ' | ' + @SQL_TYPE 

        FETCH NEXT FROM MyCursorX INTO @TABLE_NAME,@COLUMN_NAME,@SQL_TYPE
    END 
    DEALLOCATE MyCursorX
    PRINT 'END'

END

DROP TABLE zzTemp_ClassCreator


IF @CREATE_ENTIRE_CLASS = 0
    PRINT '**************************END CREATE FRAMEWORK FOR SQL STORED PROCEDURE TO UPDATE TABLE(S)*************************************************'

IF @CREATE_ENTIRE_CLASS = 1
    PRINT 'End Class'


SET NOCOUNT ON
GO
