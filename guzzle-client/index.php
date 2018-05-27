<?php
use GuzzleHttp\Client;
use GuzzleHttp\Exception\ClientException;
use GuzzleHttp\Psr7;

require 'vendor/autoload.php';

$clientGAE = new Client([
    // Base URI is used with relative requests
    'base_uri' => 'https://inf63app2.appspot.com/rest/'
]);

$clientHeroku = new Client([
    // Base URI is used with relative requests
    'base_uri' => 'https://test-app-heroku-sevik-detax.herokuapp.com/loanapproval/'
]);

error_reporting(E_ALL ^ E_NOTICE);  
?>


<html>

    <head>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>

    <h2 style="text-align: center">BANK APP</h2>

    <div class="card-two">
        <h3> Call a web service </h3>
        <form action="/" method="post">
            <div style="margin: 2% 0;">
                <span>Url</span>
                <input style="margin-left: 1%; width: 80vw" type="text" name="url">
            </div>
            <div>
                <input type="radio" id="get"
                name="method" value="GET">
                <label for="contactChoice1">GET</label>

                <input type="radio" id="post"
                name="method" value="POST">
                <label for="contactChoice2">POST</label>

                <input type="radio" id="delete"
                name="method" value="DELETE">
                <label for="contactChoice3">DELETE</label>
            </div>
            <div>
                <div class="json-badge">JSON</div>
                <textarea style="width:100%;" rows="10" name="params" value="params"> </textarea>
            </div>
            <input style="margin-top: 2%;" type="submit">
        </form> 
        <?php 
            if(isset($_POST['url']) && $_POST['method'] && $_POST['params']){
                 $clientOther = new Client([
                    'base_uri' => $_POST['url']
                ]);
                try {
                    $json = json_decode($_POST['params']);
                    $responseOther = $clientOther->request($_POST['method'], "", [ 'json' =>  $json]); 
                    $body = $responseOther->getBody();
                    echo "<b>".$body."</b>";
                } catch (ClientException $e) {
                    echo "<b>".$e->getResponse()->getBody()."</b>";
                }
            }
        ?>
    </div>

    <div style="margin:2%; display:flex; flex-direction:row; justify-content: space-between;">

        <div class="card-three">
            <h3>Accounts</h3>
            <div> 
                <?php 
                    $response = $clientGAE->request('GET', 'account/list'); 
                    $body = $response->getBody();
                    $accounts = json_decode($body);
                    echo "<ul style=\"overflow-y: auto; max-height: 28vh; margin-left: -3%\">";
                    foreach ($accounts as $account){
                        echo "<li style=\"margin: 5px 0\">";
                        echo "id : ".number_format($account->id, 0, '', '');
                        echo ", first name : ".$account->firstName;
                        echo ", last name : ".$account->lastName;
                        echo ", risk : ".$account->risk;
                        echo ", amount : ".$account->amount;
                        echo "</li>";
                    }
                    echo "</ul>";
                ?>
            </div>
        </div>
        
        <div class="card-three">
            <h3>Approvals</h3>
            <div> 
                <?php 
                    $response = $clientGAE->request('GET', 'approval/list'); 
                    $body = $response->getBody();
                    $approvals = json_decode($body);
                    echo "<ul style=\"overflow-y: auto; max-height: 28vh; margin-left: -3%\">";
                    foreach ($approvals as $approval){
                        echo "<li style=\"margin: 5px 0\">";
                        echo "id : ".number_format($approval->id, 0, '', '');
                        echo ", last name : ".$approval->lastName;

                        $decision = "";
                        if($approval->accepted == 1){
                            $decision = "accepted";
                        } else {
                            $decision = "refused";
                        }

                        echo ", decision : ".$decision;
                        echo "</li>";
                    }
                    echo "</ul>";
                ?>
            </div>
        </div>

    </div>

    <div class="card-two">
        <h3>Credit request</h3>
        <div style="padding: 0 2%">
            <form action="/" method="post">
            <div class="block">
                <label>LastName</label>
                <input type="text" name="lastname">
            </div>
            <div class="block">
                <label>Id</label>
                <input type="text" name="id">
            </div>
            <div class="block">
                <label>Amount</label>
                <input type="text" name="amount">
            </div>
            <input style="margin-top: 10px;" type="submit">
            </form> 
            <?php 
                    if(isset($_POST['lastname']) && isset($_POST['id']) && isset($_POST['amount']) ){

                        $lastname = $_POST['lastname'];
                        $id = $_POST['id'];
                        $somme = $_POST['amount'];
                        $path = 'credit/'.$lastname.'/'.$id.'/'.$somme;

                        echo "<b> URL : ".'https://test-app-heroku-sevik-detax.herokuapp.com/loanapproval/'
                                    .$path."</b><br>"."<br>";
                        
                        echo "<div style=\"width: 100%; height: 100%; border: 1px solid lightgray; padding: 1%;\">";
                    
                        try {
                            $response = $clientHeroku->request('GET', $path); 
                            $body = $response->getBody();
                            echo "<b>".$body."</b>";
                        } catch (ClientException $e) {
                            echo "<b>".$e->getResponse()->getBody()."</b>";
                        }
                        echo "</div";
                    }
            ?>
        </div>
    </div>
    
</html>