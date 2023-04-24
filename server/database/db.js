const mysql=require('mysql2')
const fs=require('fs')
var connection = mysql.createConnection({ 
    host: "sheildkavach.mysql.database.azure.com", 
    user: "sheild", 
    password: "Kavach@123", 
    database: "sheild", 
    multipleStatements: true,
    port: 3306, 
    ssl: { 
        ca: fs.readFileSync('./DigiCertGlobalRootCA.crt.pem') 
    } 
})

connection.query(
    `
        create table if not exists users(
            mobile varchar(10) not null primary key,
            userName varchar(30) not null,
            userPassword varchar(30) not null
        );
    `,(err,results)=>{
        if(err){
            console.log(err)
        }
        else{
            console.log("Created users")
        }
    }
)
connection.query(
    `
        create table if not exists volunteer(
            mobile varchar(10) not null primary key,
            userName varchar(30) not null,
            userPassword varchar(30) not null
        );
    `,(err,results)=>{
        if(err){
            console.log(err)
        }
        else{
            console.log("created Volunteer")
        }
    }
)
connection.query(
    `
        create table if not exists contacts(
            id int primary key auto_increment,
            userMobile varchar(10) not null,
            contactMobile varchar(10) not null,
            foreign key(userMobile) references users(mobile)
        );
    `,(err,results)=>{
        if(err){
            console.log(err)
        }
        else{
            console.log("Created contacts")
        }
    }
)
module.exports={
    connection
}