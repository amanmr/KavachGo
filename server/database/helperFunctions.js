const {connection}=require('./db')

function addUser(name,mobile,password,done){
    //for volunteer user
    connection.query(
        `
            select * from users where mobile="${mobile}";
        `
        ,(err,results)=>{
            if(results.length!=0){
                done(err,{ "output": "User Already Exists"})
            }
            else{
                var tempnum=String(mobile)
                connection.query(
                    `
                        insert into users
                        values("${tempnum}","${name}","${password}");
                    `
                    ,(err,results)=>{
                        done(err,{"output":"User Created"})
                    }
                )
            }
        }
    )
}

function addVolunteer(name,mobile,password,done){
    //for volunteer user
    connection.query(
        `
            select * from volunteer where mobile="${mobile}";
        `
        ,(err,results)=>{
            if(results.length!=0){
                done(err,{ "output": "User Already Exists"})
            }
            else{
                var tempnum=String(mobile)
                connection.query(
                    `
                        insert into volunteer
                        values("${tempnum}","${name}","${password}");
                    `
                    ,(err,results)=>{
                        done(err,{"output":"User Created"})
                    }
                )
            }
        }
    )
}



function loginUser(mobile,password,done){
    //for volunteer user
    
    connection.query(
        `
            select * from users 
            where mobile="${mobile}" and userPassword="${password}";
            select * from volunteer 
            where mobile="${mobile}" and userPassword="${password}";
        `
        ,(err,results)=>{
            done(err,results)
        }
    )
}

module.exports={
    addUser,loginUser,addVolunteer
}
