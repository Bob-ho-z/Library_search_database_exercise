import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.Scanner;



class Main {
    
    static String toDate(String s){
        if ( s.split("/").length==3) return String.format("str_to_date('%s','%%d/%%m/%%Y')", s);
        else return "null";
    }
    
    public static void searchfunction(Connection con,String psql) throws SQLException{
        ResultSet bookresult = getSQLResult(con,psql);
        if(!bookresult.isBeforeFirst())
  	      System.out.println("No records found.");
        else
   	      while(bookresult.next()){
  		      //System.out.print(bookresult.getString("title"));
            int bcid = bookresult.getInt("bcid");
            String CallID = bookresult.getString("callnum");
            //get book category
            psql = "SELECT bcname FROM book_category WHERE bcid ='" + bcid + "';";
            ResultSet bookcategoryresult = getSQLResult(con,psql);
            bookcategoryresult.next();
            String book_category  = bookcategoryresult.getString("bcname");
            //get author
            psql = "SELECT aname FROM authorship WHERE callnum ='" + CallID + "';";
            ResultSet authorshipresult = getSQLResult(con,psql);
            authorshipresult.next();
            String aname  = authorshipresult.getString("aname");
            //get copy num
            psql = "SELECT copynum FROM copy WHERE callnum ='" + CallID + "';";
            ResultSet copyresult = getSQLResult(con,psql);
            copyresult.next();
            int copynum  = copyresult.getInt("copynum");
                    
            // print result
                    
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy|");
            String printresult = "|" + bookresult.getString("callnum") + "|" + bookresult.getString("title") + "|" + book_category + "|" + aname + "|" + bookresult.getFloat("rating") + "|" + copynum + "|";
            System.out.println(printresult); 
     }
            System.out.println("End of Query");

     }
    public static ResultSet getSQLResult(Connection con,String psql) throws SQLException{
        PreparedStatement pstmt = con.prepareStatement(psql);
        //pstmt.setString(1,CallID);
        ResultSet result = pstmt.executeQuery();
        return result;
        
    }

    static void librarianMenu(){
        System.out.println("");
        System.out.println("-----Operations for librarian menu-----");
        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. List all un-returned book copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
    }


    public static void main(String[] args) {
        String dbAddess = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db17";
        String dbUsername = "Group17";
        String dbPassword = "CSCI3170DB17";
        Scanner scan = new Scanner(System.in);

        
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddess,dbUsername, dbPassword);
            String sql = "SELECT * FROM T1";
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSet rs = null;
            
            if(!resultSet.isBeforeFirst())
                System.out.println("No record");
            else 
                while(resultSet.next()){
                    System.out.print(resultSet.getString(2));
                    System.out.print(resultSet.getInt(1));
                }
            
            int result = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS T2 (id INT)");
            
            while(true){
                Scanner sc = new Scanner(System.in);
                try{
                    System.out.println("\n-----Main Menu-----\n");
                    System.out.println("What kinds of operations would you like to perform?");
                    System.out.println("1. Operations for Administrator");
                    System.out.println("2. Operations for Library users");
                    System.out.println("3. Operations for Operations for Librarian");
                    System.out.println("4. Exit this program");
                    System.out.print("Enter Your Choice: ");
                    
                    Integer index0; 
                    index0 = sc.nextInt();
                    switch (index0) {
                        case 1:
                            while (true) {
                                boolean flag;
                                flag=false;
                                
                                try{
                                    
                                    System.out.println("\n-----Operations for administrator menu-----\n");
                                    System.out.println("What kinds of operations would you like to perform?");
                                    System.out.println("1. Create all tables");
                                    System.out.println("2. Delete all tables");
                                    System.out.println("3. Load from datafile");
                                    System.out.println("4. Show number of records in each table");
                                    System.out.println("5. Return to the main menu");
                                    System.out.print("Enter Your Choice: ");
                                    
                                    Integer index1; 
                                    index1 = sc.nextInt();
                                    switch (index1) {
                                        case 1:
                                        
                                            String sql111 = "CREATE TABLE IF NOT EXISTS user_category (ucid INT, max INT, period INT,PRIMARY KEY (ucid));";
                                        
                                            String sql112 = "CREATE TABLE IF NOT EXISTS libuser (libuid CHAR(10), name VARCHAR(25), age INT, address VARCHAR(100), ucid INT,PRIMARY KEY (libuid),FOREIGN KEY (ucid) REFERENCES user_category(ucid));";
                                        
                                            String sql113 = "CREATE TABLE IF NOT EXISTS book_category (bcid INT, bcname VARCHAR(30),PRIMARY KEY (bcid));";
                                        
                                            String sql114 = "CREATE TABLE IF NOT EXISTS book (callnum CHAR(8), title VARCHAR(30), publish DATE, rating FLOAT, tborrowed INT, bcid INT,PRIMARY KEY (callnum),FOREIGN KEY (bcid) REFERENCES book_category(bcid));";
                                        
                                            String sql115 = "CREATE TABLE IF NOT EXISTS copy (callnum CHAR(8), copynum INT, CONSTRAINT pk_copy PRIMARY KEY (callnum, copynum),FOREIGN KEY (callnum) REFERENCES book(callnum));";
                                        
                                            String sql116 = "CREATE TABLE IF NOT EXISTS borrow (libuid CHAR(10), callnum CHAR(8), copynum INT, checkout DATE, returndate DATE, CONSTRAINT pk_borrow PRIMARY KEY (libuid, callnum, copynum, checkout),FOREIGN KEY (libuid) REFERENCES libuser(libuid), CONSTRAINT fk_copy FOREIGN KEY (callnum,copynum) REFERENCES copy(callnum,copynum));";
                                        
                                            String sql117 = "CREATE TABLE IF NOT EXISTS authorship (aname VARCHAR(25), callnum CHAR(8), CONSTRAINT pk_authership PRIMARY KEY (aname, callnum),FOREIGN KEY (callnum) REFERENCES book(callnum));";
                                        
                                            int result111 = stmt.executeUpdate(sql111);
                                            int result112 = stmt.executeUpdate(sql112);
                                            int result113 = stmt.executeUpdate(sql113);
                                            int result114 = stmt.executeUpdate(sql114);
                                            int result115 = stmt.executeUpdate(sql115);
                                            int result116 = stmt.executeUpdate(sql116);
                                            int result117 = stmt.executeUpdate(sql117);
                                        
                                            break;
                                        
                                        case 2:                                            
                                            int result121,result122,result123,result124,result125,result126,result127;
                                            String sql121="DROP TABLE IF EXISTS user_category;";
                                            String sql122="DROP TABLE IF EXISTS libuser;";
                                            String sql123="DROP TABLE IF EXISTS book_category;";
                                            String sql124="DROP TABLE IF EXISTS book;";
                                            String sql125="DROP TABLE IF EXISTS copy;";
                                            String sql126="DROP TABLE IF EXISTS borrow;";
                                            String sql127="DROP TABLE IF EXISTS authorship;";
                                            result127 = stmt.executeUpdate(sql127);
                                            result126 = stmt.executeUpdate(sql126);
                                            result125 = stmt.executeUpdate(sql125);
                                            result124 = stmt.executeUpdate(sql124);
                                            result123 = stmt.executeUpdate(sql123);
                                            result122 = stmt.executeUpdate(sql122);
                                            result121 = stmt.executeUpdate(sql121);
                                            break;
                                        case 3:
                                            System.out.print("Please enter the path of data: ");
                                            String directory = sc.next();
                                            
                                        
                                        
                                            //user_category
                                            File f131 = new File(directory+"/user_category.txt");
                                            InputStreamReader reader131 = new InputStreamReader(new FileInputStream(f131));
                                            BufferedReader br131 = new BufferedReader(reader131);
                                            String line131 = "";
                                            String[] blocks131;
                                        
                                            while ((line131=br131.readLine())!=null){
                                                blocks131=line131.split("\t");
                                                sql=String.format("INSERT INTO user_category VALUES (%s,%s,%s);",blocks131[0],blocks131[1],blocks131[2]);
                                                //System.out.println(sql);
                                                stmt.executeUpdate(sql);
                                            }
                                        
                                            //book_category
                                            File f132 = new File(directory+"/book_category.txt");
                                            InputStreamReader reader132 = new InputStreamReader(new FileInputStream(f132));
                                            BufferedReader br132 = new BufferedReader(reader132);
                                            String line132 = "";
                                            String[] blocks132;
                                        
                                            while ((line132=br132.readLine())!=null){
                                                blocks132=line132.split("\t");
                                                sql=String.format("INSERT INTO book_category VALUES (%s,'%s');",blocks132[0],blocks132[1]);
                                                //System.out.println(sql);
                                                stmt.executeUpdate(sql);
                                            }
                                            
                                            //user
                                            File f133 = new File(directory+"/user.txt");
                                            InputStreamReader reader133 = new InputStreamReader(new FileInputStream(f133));
                                            BufferedReader br133 = new BufferedReader(reader133);
                                            String line133 = "";
                                            String[] blocks133;
                                        
                                            while ((line133=br133.readLine())!=null){
                                                blocks133=line133.split("\t");
                                                sql=String.format("INSERT INTO libuser VALUES ('%s','%s',%s,'%s',%s);",blocks133[0],blocks133[1],blocks133[2],blocks133[3].replace("'", "\\'"),blocks133[4]);
                                                //System.out.println(sql);
                                                stmt.executeUpdate(sql);
                                            }
                                        
                                            //book&copy&Authorship
                                            File f134 = new File(directory+"/book.txt");
                                            InputStreamReader reader134 = new InputStreamReader(new FileInputStream(f134));
                                            BufferedReader br134 = new BufferedReader(reader134);
                                            String line134 = "";
                                            String[] blocks134;
                                        
                                            while ((line134=br134.readLine())!=null){
                                                blocks134=line134.split("\t");
                                                sql=String.format("INSERT INTO book VALUES ('%s','%s',%s,%s,%s,%s);", blocks134[0], blocks134[2],  toDate(blocks134[4]), blocks134[5], blocks134[6], blocks134[7]);
                                                //System.out.println(sql);
                                                stmt.executeUpdate(sql);
                                                
                                                
                                                
                                                int i,copynumbers=Integer.parseInt(blocks134[1]);
                                                
                                                for (i = 1; i<= copynumbers;i++){
                                                    sql=String.format("INSERT INTO copy VALUES ('%s',%d);", blocks134[0],i);
                                                    //System.out.println(sql);
                                                    stmt.executeUpdate(sql);
                                                }
                                                
                                                String[] blocks1343=blocks134[3].split(",");
                                                int authornumber=blocks1343.length;
                                                for (i=0;i<authornumber;i++){
                                                    sql=String.format("INSERT INTO authorship VALUES ('%s','%s');", blocks1343[i], blocks134[0]);
                                                    //System.out.println(sql);
                                                    stmt.executeUpdate(sql);
                                                    
                                                }
                                                
                                                
                                            }
                                        
                                            //check_out
                                            File f135 = new File(directory+"/check_out.txt");
                                            InputStreamReader reader135 = new InputStreamReader(new FileInputStream(f135));
                                            BufferedReader br135 = new BufferedReader(reader135);
                                            String line135 = "";
                                            String[] blocks135;
                                        
                                            while ((line135=br135.readLine())!=null){
                                                blocks135=line135.split("\t");
                                                sql=String.format("INSERT INTO borrow VALUES ('%s','%s',%s,%s,%s);",blocks135[2],blocks135[0],blocks135[1],toDate(blocks135[3]),toDate(blocks135[4]));
                                                //System.out.println(sql);
                                                stmt.executeUpdate(sql);
                                            }
                                        
                                            break;
                                        
                                        case 4:
                                            String sql141="SELECT COUNT(*) FROM user_category;";
                                            String sql142="SELECT COUNT(*) FROM libuser;";
                                            String sql143="SELECT COUNT(*) FROM book_category;";
                                            String sql144="SELECT COUNT(*) FROM book;";
                                            String sql145="SELECT COUNT(*) FROM copy;";
                                            String sql146="SELECT COUNT(*) FROM borrow;";
                                            String sql147="SELECT COUNT(*) FROM authorship;";
                                            ResultSet resultSet141 = stmt.executeQuery(sql141);
                                            resultSet141.next();
                                            System.out.printf("user_category: %s\n",resultSet141.getString(1));
                                        
                                            ResultSet resultSet142 = stmt.executeQuery(sql142);
                                            resultSet142.next();
                                            System.out.printf("libuser: %s\n",resultSet142.getString(1));
                                        
                                            ResultSet resultSet143 = stmt.executeQuery(sql143);
                                            resultSet143.next();
                                            System.out.printf("book_category: %s\n",resultSet143.getString(1));
                                        
                                            ResultSet resultSet144 = stmt.executeQuery(sql144);
                                            resultSet144.next();
                                            System.out.printf("book: %s\n",resultSet144.getString(1));
                                        
                                            ResultSet resultSet145 = stmt.executeQuery(sql145);
                                            resultSet145.next();
                                            System.out.printf("copy: %s\n",resultSet145.getString(1));
                                        
                                            ResultSet resultSet146 = stmt.executeQuery(sql146);
                                            resultSet146.next();
                                            System.out.printf("borrow: %s\n",resultSet146.getString(1));
                                        
                                            ResultSet resultSet147 = stmt.executeQuery(sql147);
                                            resultSet147.next();
                                            System.out.printf("authorship: %s\n",resultSet147.getString(1));
                                        
                                        
                                            break;
                                        case 5:
                                            flag=true;
                                        default:
                                            throw new InputMismatchException("InputOutOfBound");
                                            
                                    }
                                    
                                    
                                }
                                catch(FileNotFoundException e){
                                    System.out.println("File is not found.");
                                }
                                catch (IOException e) {
                                    System.out.println("IOExeption");
                                }
                                catch(InputMismatchException e){
                                    System.out.println("Please enter a valid index.");
                                }
                                finally{
                                    if (flag) break;
                                    else continue;
                                }
                                
                            }                           
                        
                            break;
                        case 2:
                            Boolean userFlag = true;
                            while(userFlag){

                                System.out.println("-----Operations for library user menu-----");
                                System.out.println("What kind of operations would you like to perform?");
                                System.out.println("1. Search for Books");
                                System.out.println("2. Show loan records of a user");
                                System.out.println("3. Return to main menu");
                                System.out.print("Enter your choice: ");
                                
                                int choice2 = scan.nextInt();
                                System.out.print("\n");
                                
                                if(choice2 == 1)
                                {
                                    System.out.println("Choose the search criterion:");
                                    System.out.println("1. call number");
                                    System.out.println("2. title");
                                    System.out.println("3. author");
                                    System.out.print("Choose the search criterion:");
                                    int choice3 = scan.nextInt();
                                    System.out.print("\n");
                                    //call number
                                    if(choice3 == 1)
                                    {
                                    System.out.print("Type in the search keyword:");
                                    scan.nextLine();
                                    String CallID = scan.nextLine();
                                    System.out.print("\n");
                                    String psql = "SELECT callnum, title, rating, bcid FROM book WHERE callnum = '" + CallID + "';" ;
                                    searchfunction(con,psql);
                                        
                                        }

                                    
                                    
                                    
                                    if(choice3 == 2){
                                    System.out.print("Type in the search keyword:");
                                    scan.nextLine();
                                    String title = scan.nextLine();
                                    System.out.print("\n");
                                    String psql = "SELECT callnum, title, rating, bcid FROM book WHERE title LIKE '%" + title + "%';" ;
                                    searchfunction(con,psql);
                                    
                                    
                                    }
                                    
                                    if(choice3 == 3)
                                    {
                                    System.out.print("Type in the search keyword:");
                                    scan.nextLine();
                                    String author = scan.nextLine();
                                    System.out.print("\n");
                                    
                                    String psql = "SELECT callnum FROM authorship WHERE aname LIKE '%" + author + "%';";
                                    ResultSet authorshipresult = getSQLResult(con,psql);
                                    if(!authorshipresult.isBeforeFirst())
                                        System.out.println("No records found.");
                                    else
                                    
                                    while(authorshipresult.next()){
                                    
                                    String CallID  = authorshipresult.getString("callnum");
                                    
                                    psql = "SELECT callnum, title, rating, bcid FROM book WHERE callnum = '" + CallID + "';" ;
                                    searchfunction(con,psql);
                                    }
                                    }
                                    
                                    
                                }
                                
                                else if(choice2 == 2)
                                {
                                    System.out.println("Enter your User ID");
                                    scan.nextLine();
                                    String uid = scan.nextLine();
                                    System.out.print("\n");
                                    String psql = "SELECT libuid, callnum, copynum, checkout, returndate FROM borrow WHERE libuid = '" + uid + "' ORDER BY checkout DESC;" ;
                                    ResultSet borrowresult = getSQLResult(con,psql);
                                    if(!borrowresult.isBeforeFirst())
                                    System.out.println("No records found.");
                                    else
                                    while(borrowresult.next()){
                                        String CallID = borrowresult.getString("callnum");
                                        //System.out.println("the callnum is:");
                                        //System.out.println(CallID);
                                        // get book title
                                        psql = "SELECT title FROM book WHERE callnum = '" + CallID + "';" ;
                                        ResultSet bookresult = getSQLResult(con,psql);
                                        bookresult.next();
                                        
                                        // get author
                                        psql = "SELECT aname FROM authorship WHERE callnum = '" + CallID + "';" ;
                                        ResultSet authorshipresult = getSQLResult(con,psql);
                                        authorshipresult.next(); 
                                        
                                        
                                        // sort descending order
                                        
                                        
                                        
                                        // print result
                                        String reresult;
                                        if(borrowresult.getString("returndate") == null)
                                        reresult = "No";
                                        else 
                                        reresult = "Yes";
                                        
                                        
                                        System.out.println("Loan Record:");
                                        System.out.println("|Call Num|CopyNum|Title|Author|Check-out|Returned?|");
                                        String printresult = "|" + borrowresult.getString("callnum") + "|" + borrowresult.getInt("copynum") + "|" + bookresult.getString("title") + "|" + authorshipresult.getString("aname") + "|" + borrowresult.getString("checkout") + "|" + reresult + "|";
                                        System.out.println(printresult);
                                        System.out.println("End of Query");

                                                
                                    }
                                }
                                else if(choice2 == 3)
                                {
                                    userFlag = false;
                                    
                                } 
                                
                                }

                            break;
                        case 3:
                            boolean librarianFlag = true;
                            while(librarianFlag){
                                librarianMenu();
                                System.out.print("Enter Your Choice: ");
                                int yourChoice = Integer.parseInt(System.console().readLine());

                                switch(yourChoice){
                                    // Borrow a book copy
                                    case 1:
                                        System.out.print("Enter The User ID: ");
                                        String userID = System.console().readLine();
                                        System.out.print("Enter The Call Number: ");
                                        String callNumber = System.console().readLine();
                                        System.out.print("Enter The Copy Number: ");
                                        int copyNumber = Integer.parseInt(System.console().readLine());
                                
                                        // check whether that book copy is available to be borrowed
                                        sql = "SELECT returndate FROM borrow WHERE callnum = '%s' and copynum = %d";
                                        sql = String.format(sql, callNumber, copyNumber);
                                        stmt = con.createStatement();
                                        rs = stmt.executeQuery(sql);
                                                    
                                        boolean check_available = true;
                                                    
                                        while(rs.next()){
                                            if(rs.getString(1) == null){
                                                check_available = false;
                                            }
                                        }
                                
                                        if(check_available == false){
                                            System.out.println("[Error]: The book copy is not returned.");
                                        }
                                        else{
                                            sql = "INSERT INTO borrow (libuid, callnum, copynum, checkout, returndate) VALUES (" + "'" + userID + "'" + ", " + "'" + callNumber + "'" + ", " + copyNumber + ", " + "'" + java.time.LocalDate.now() + "'" + ", null)";
                                            stmt = con.createStatement();
                                            stmt.executeUpdate(sql);
                                            System.out.println("Book borrowing performed successfully.");
                                        }
                                        break;
                                    
                                    // Return a book copy
                                    case 2:
                                        System.out.print("Enter The User ID: ");
                                        userID = System.console().readLine();
                                        System.out.print("Enter The Call Number: ");
                                        callNumber = System.console().readLine();
                                        System.out.print("Enter The Copy Number: ");
                                        copyNumber = Integer.parseInt(System.console().readLine());
                                        System.out.print("Enter Your Rating of the Book: ");
                                        int userRating = Integer.parseInt(System.console().readLine());

                                        // check whether the book is already borrowed
                                        sql = "SELECT * FROM borrow WHERE libuid = '%s' and callnum = '%s' and copynum = %s";
                                        sql = String.format(sql, userID, callNumber, copyNumber);
                                        stmt = con.createStatement();
                                        rs = stmt.executeQuery(sql);

                                        if(!rs.isBeforeFirst()){
                                            System.out.println("[Error]: A matching borrow record is not found. The book has not been borrowed yet.");
                                        }
                                        else{
                                            // update the return date
                                            sql = "UPDATE borrow SET returndate = " + "'" + java.time.LocalDate.now() + "'" + " WHERE libuid = '%s' and callnum = '%s' and copynum = %s";
                                            sql = String.format(sql, userID, callNumber, copyNumber);
                                            con.createStatement().executeUpdate(sql);

                                            sql = "SELECT rating, tborrowed FROM book WHERE callnum = '%s'";
                                            sql = String.format(sql, callNumber);
                                            rs = con.createStatement().executeQuery(sql);
                                            
                                            rs.next();
                                            String rating_str = rs.getString(1);
                                            float book_rating = 0;
                                            if(rating_str != null){
                                                book_rating = Float.parseFloat(rating_str);
                                            }
                                            int borrow_times = Integer.parseInt(rs.getString(2));
                                            float new_book_rating = (book_rating*borrow_times+userRating)/(borrow_times+1);
                                            int new_borrow_times = borrow_times + 1;

                                            //update the rating and the number of times borrowed of the book
                                            sql = "UPDATE book SET rating = " + new_book_rating + ", " + "tborrowed = " + new_borrow_times + " WHERE callnum = '%s'";
                                            sql = String.format(sql, callNumber);
                                            con.createStatement().executeUpdate(sql);
                                            System.out.println("Book returning performed successfully.");
                                        }
                                        break;
                                    
                                    // list all un-returned book copies which are checked-out within a period
                                    case 3:
                                        System.out.print("Type in the starting date [dd/mm/yyyy]: ");
                                        String startDate = System.console().readLine();
                                        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
                                        String endDate = System.console().readLine();

                                        sql = "SELECT * FROM borrow WHERE returndate IS NULL AND (checkout BETWEEN STR_TO_DATE('" + startDate + "', '%d/%m/%Y') AND STR_TO_DATE('" + endDate + "', '%d/%m/%Y')) ORDER BY checkout DESC";
                                        rs = con.createStatement().executeQuery(sql);
                                        if(!rs.isBeforeFirst()){
                                            System.out.println("[Error]: There aren't any un-returned book copies which are checked-out within this period.");
                                        }
                                        else{
                                            System.out.println("|LibUID|CallNum|CopyNum|Checkout|");
                                            while(rs.next()){
                                                for(int i = 1; i <= 4; i++){
                                                    System.out.print("|");
                                                    String colVal = rs.getString(i);
                                                    System.out.print(colVal);
                                                }
                                                System.out.println("|");
                                            }
                                        }
                                        
                                        System.out.println("End of Query");
                                        break;
                                    
                                        case 4:
                                            librarianFlag = false;

                            }
                            }
                            break;
                        case 4:
                            System.exit(0);
                            break;
                        default:
                            throw new InputMismatchException("InputOutOfBound");
                    }
                }
                catch(InputMismatchException e){
                    System.out.println("Please enter a valid index.");
                }
                finally {
                    continue;
                }
            }
            
            
        }catch (ClassNotFoundException e){
            System.out.println("Error1: No Driver");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        
        
        
    }
}