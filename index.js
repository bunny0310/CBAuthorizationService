const express = require("express");
const cors = require("cors");
//npm packages - import libraries
const body_parser = require("body-parser");
const passport =require("passport");
const path = require("path");
const jwt = require('jsonwebtoken');
const axios = require('axios');

// port configuration
let PORT = process.env.PORT||3000;
const cmdPort = process.argv[2] ? process.argv[2].slice(process.argv[2].length - 4, process.argv[2].length) : null;
if (!(cmdPort === undefined || cmdPort === null)) {
  PORT = cmdPort;
}
const session = require("express-session");

// create a new express application
const app = express();

// cors configuration
const validOrigins = [
];
app.use(cors({
    origin: (origin, callback) => {
        if (validOrigins.indexOf(origin) === -1) {
            callback(null, true)
          } else {
            callback(new Error('Not allowed by CORS'))
          }
    },
    credentials: true,
    allowedHeaders: 'Content-Type,Authorization'
  }));

app.use(session({
    secret: 'secrettexthere',
    saveUninitialized: true,      
    resave: true,
    cookie : {
    sameSite: 'none',
    secure: true
  }
}));

//set application configuration settings
app.use(body_parser.json())
app.use(express.static(path.join(__dirname,'/public')));
app.use(passport.initialize());
app.use(passport.session());
app.set('trust proxy', 1);

// routes

app.post('/api/v1/auth/', (req, res) => {

    const body = req.body;
    if(body.email === undefined || body.email === null || body.password === null || body.password === undefined) {
        return res.status(400).json({"msg": "invalid format"});
    }

    const userDetails = {
        email: body.email,
        password: body.password
    };
    axios.post('http://ramen-users-service.herokuapp.com/api/v1/users/authorize', userDetails)
    .then((response) => {
        if(response.status === 201) {
            const payload = {
                username: userDetails.email
            }
            const options = {
                subject: userDetails.email,
                expiresIn: 3600
            }
            const token = jwt.sign(payload, 'secret123', options);
            return res.status(200).json({"data": token});
        }
        if(response.status === 401) {
            return res.status(401).json({"msg": "Authentication failed."});
        }
        if(response.status === 500) {
            return res.status(500).json({"msg": "Internal server error."});
        }
    }).catch((err) => {
        console.log(err.response);
        return res.status(err.response.status).json(err.response.data);
    })
})

// listen 
app.listen(PORT, ()=>{
    console.log("Running on port ", PORT);
});