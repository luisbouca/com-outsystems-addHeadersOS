var fs = require('fs');
var path = require('path');

const constants={
    javaSrcPath : path.join("platforms","android","app","src","main","java"),
    kotlinSrcPath : path.join("platforms","android","app","src","main","kotlin"),
    pluginID : "com.outsystems.uaepass"
}

module.exports = function (context) {
    
    console.log("Start changing Files!");
    var Q = require("q");
    var deferral = new Q.defer();

    var pathArray = path.join(constants.javaSrcPath,"com","outsystems","plugins","loader","engines","UAEPass.java")
    
    if (fs.existsSync(pathArray)) {
        var content = fs.readFileSync(pathArray, "utf8");

        content = content.replace("com.outsystems.plugins.loader.clients.WebClient","com.outsystems.addheadersos.MyWebClient");
        content = content.replaceAll("WebClient","MyWebClient");

        
        fs.writeFileSync(pathArray, content);
        console.log("Finished changing "+path.basename(pathArray)+"!");
    }else{
        console.error("Error could not find "+path.basename(pathArray)+"!");
    }

    console.log("Finished changing Files!");
    
    deferral.resolve();

    return deferral.promise;
}