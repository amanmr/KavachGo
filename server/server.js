const express=require('express')
const {addUser,loginUser,addVolunteer}=require('./database/helperFunctions')
const app=express()
const http=require('http')
const socketio=require('socket.io')
app.use(express.urlencoded({extended:true}))



const server = http.createServer(app)
const io=socketio(server)

io.on('connection',(socket)=>{
    console.log(socket)

    socket.on("trackMe",(data)=>{
        console.log(data)
        socket.emit("trackMe",data)
    })
})


app.post('/registeruser',(req,res)=>{
    
    try{
        addUser(req.body.name,req.body.mobile,req.body.password,(err,results)=>{
            if(err){
                console.log(err)
                res.status(500).send(err)
            }
            else{
                res.send(results)
            }
        })
    }
    catch (err){
        console.error(err)
        res.send(Object("output","Error"))
    }
})


app.post('/registervolunteer',(req,res)=>{
    
    try{
        addVolunteer(req.body.name,req.body.mobile,req.body.password,(err,results)=>{
            if(err){
                console.log(err)
                res.status(500).send(err)
            }
            else{
                res.send(results)
            }
        })
    }
    catch (err){
        console.error(err)
        res.send(Object("output","Error"))
    }
})

app.post('/login',(req,res)=>{
    try{
        loginUser(req.body.mobile,req.body.userPassword,(err,results)=>{
            if (err) {
                console.error(err)
                res.status(500).send({"output":"Error occured"})
            }
            else {
                if (results[0].length > 0 || results[1].length > 0) {
                    var resp={"output":"LoggedIn"}
                    if(results[0].length>0){
                        resp.name=results[0][0].userName
                    }
                    else{
                        resp.name=results[0][0].userName
                    }
                    console.log(resp)
                    res.send(resp)
                }
                else {
                    res.send({"output":"User Doesnot Exist",
                "name":""})
                }
            }
        })
    }
    catch (err){
        console.error(err)
        res.send(Object("output","Error"))
    }
    
})

app.get('/',(req,res)=>{
    res.send("Hello World")
})
app.listen(process.env.PORT,()=>{
    console.log("Server started at port http://localhost:4444")
})