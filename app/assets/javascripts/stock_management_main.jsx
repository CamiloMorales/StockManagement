var MainContainer = React.createClass({
    getInitialState: function () {
        return {
            data: null
        }
    },
    componentDidMount: function () {
        this.loadProducts();
    },
    reloadProducts: function () {
        this.loadProducts();
    },
    loadProducts: function () {
        var searchUrl = "/stock/getAllProducts"
        $.ajax({
            url: searchUrl,
            dataType: 'json',
            cache: false,
            success: function (data) {
                this.setState({data: data["result"]});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    render: function () {
        if (this.state.data && this.state.data !== undefined) {
            return (
                <div>
                    <h1>STOCK MONITORING PANEL</h1>
                    <div>
                        <AddProduct reloadProducts={this.reloadProducts}/>
                        <ResultTable result={this.state.data} reloadProducts={this.reloadProducts}/>
                        <CustomerPanel result={this.state.data} reloadProducts={this.reloadProducts}/>
                    </div>
                </div>
            );
        }
        return (
                <div>
                    LOADING!!!
                </div>
            );
    }
});

var AddProduct = React.createClass({
    addProduct: function (e) {
        e.preventDefault();
        var refillUrl = "/stock/addProduct?productName="+this.state.product_name+"&currentQuantity="+this.state.quantity
        $.ajax({
            url: refillUrl,
            dataType: 'json',
            cache: false,
            success: function (rows) {
                if(rows["result"]){
                    console.log("PRODUCT REFILLED CORRECTLY.")
                    this.props.reloadProducts()
                }else{
                    alert("The specified product already exists.")
                }
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    handleChangeName: function(event) {
        this.setState({product_name: event.target.value});
    },
    handleChangeQuantity: function(event) {
        this.setState({quantity: event.target.value});
    },
    render:function(){
        return( <div className="panel panel-default">
                <h3>ADD PRODUCT PANEL</h3>
                    <form onSubmit={this.addProduct}>
                        <label>
                            Product name:
                            <input type="text" onChange={this.handleChangeName}/>
                        </label>
                        <label>
                            Quantity:
                            <input type="text" onChange={this.handleChangeQuantity}/>
                        </label>
                        <input type="submit" value="Add Product" />
                    </form>
                </div>
        );
    }
});

var ResultTable = React.createClass({
    reloadProductsNested: function () {
        this.props.reloadProducts()
    },
    render:function(){
        if(this.props.result){
            var result = this.props.result.map(function(result,index){
                return <ResultItem key={index} product={ result } reloadProducts={this.reloadProductsNested}/>
            }, this);
            return(
                <div className="panel panel-default">
                    <h3>CURRENT PRODUCT STATUS</h3>
                        <div className="row">
                            <div className="col-md-12">
                                <table className="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th className="col-md-4">Product id</th>
                                        <th>Product name</th>
                                        <th>Current quantity</th>
                                        <th>Refill</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        {result}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                </div>
            );
        }
        return (
            <div>
                LOADING!!!
            </div>
        );

    }
});

var ResultItem = React.createClass({
    reloadProductsNested: function () {
        this.props.reloadProducts()
    },
    refill: function (e) {
        e.preventDefault();

        var refillUrl = "/stock/refillProduct?prod_id="+this.props.product.id+"&quantity="+this.state.refill_number
        $.ajax({
            url: refillUrl,
            dataType: 'json',
            cache: false,
            success: function (rows) {
                console.log("PRODUCT REFILLED CORRECTLY.")
                this.reloadProductsNested()
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    handleChange: function(event) {
        this.setState({refill_number: event.target.value});
    },
    render:function(){
        return(
            <tr >
                <td>{this.props.product.id}</td>
                <td>{this.props.product.productName}</td>
                <td>{this.props.product.currentQuantity}</td>
                <td>
                    <form onSubmit={this.refill}>
                        <label>
                            How many items?
                            <input type="text" onChange={this.handleChange}/>
                        </label>
                        <input type="submit" value="REFILL" />
                    </form>
                </td>
            </tr>
        );
    }
});

var CustomerPanel = React.createClass({
    render:function(){
        return(
                <div className="panel panel-default">
                    <h3>CUSTOMER PANEL</h3>
                    <Login_panel result={this.props.result} reloadProducts={this.props.reloadProducts}/>
                </div>
            );
    }
});

var Login_panel = React.createClass({
    getInitialState: function () {
        return {
            username: "",
            logedin: false,
            transactions: []
        };
    },
    handleLogin: function(event) {
        var refillUrl = "/stock/customerExists?username="+this.state.customer_username
        $.ajax({
            url: refillUrl,
            dataType: 'json',
            cache: false,
            success: function (data) {
                if(data["result"]){
                    alert("Login succesfull!")
                    this.loadTransactions(this.state.customer_username)
                    this.setState({username:this.state.customer_username,
                        logedin:true})
                }else{
                    alert("This username doesn't exists. Please Sign-in with or Log-in with a existing username.")
                    this.setState({logedin:false})
                }
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    handleSignIn: function(event) {
        var signInUrl = "/stock/customerSignIn?username="+this.state.customer_username
        $.ajax({
            url: signInUrl,
            dataType: 'json',
            cache: false,
            success: function (data) {
                if(data["result"]){
                    alert("Sign-in succesfull!")
                    this.setState({username:this.state.customer_username,
                                    logedin:true,
                                   transactions:[] })
                }else{
                    alert("This user already exists. PLease Log-in, or Sign-in with another username.")
                    this.setState({logedin:false})
                }
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    loadTransactions: function () {
        var searchUrl = "/stock/getAllTransactionsByCustomer?username="+this.state.customer_username
        $.ajax({
            url: searchUrl,
            dataType: 'json',
            cache: false,
            success: function (data) {
                this.setState({transactions: data["result"]});
            }.bind(this),
            error: function (xhr, status, err) {
                alert("There was an error. Please try again later.")
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    handleChangeName: function(event) {
        this.setState({customer_username: event.target.value});
    },
    render: function() {
            return (
                <div>
                    <div>
                        <label>
                            Customer username: <input type="text" onChange={this.handleChangeName}/>
                        </label>
                        <button type="button" onClick={this.handleLogin}>Login</button>
                        or
                        <button type="button" onClick={this.handleSignIn}>Signin (new user)</button>
                    </div>
                    { (this.state.logedin ? <div> <Transaction_panel reloadProducts={this.props.reloadProducts} refreshTransactions={this.loadTransactions} logedin={this.state.logedin} username={this.state.username} products={this.props.result}/>
                                                  <Transactions_table transactions={this.state.transactions} reloadTransactionsTable={this.loadTransactions}/> </div> : <div></div>) }

                </div>
            );
    }
});

var Transaction_panel = React.createClass({
    getInitialState: function () {
        return {
            prod_selected: 1,
            quantity: ""
        };
    },
    handleProductSelection: function(event) {
        event.preventDefault()
        this.setState({prod_selected: event.target.value});
    },
    handleChangeQuantity: function(event) {
        event.preventDefault()
        this.setState({quantity: event.target.value});
    },
    handleBuy: function(event) {
        var prods = this.props.products
        for (var i = 0; i < prods.length; i++){
            var obj = prods[i];
            if(obj["id"] == this.state.prod_selected && obj["currentQuantity"] < this.state.quantity){
                alert("NOT ENOUGH ITEMS OF THIS PRODUCT.")
                return;
            }
        }

        var buyUrl = "/stock/buyProduct?username="+this.props.username+"&prod_Id="+this.state.prod_selected+"&quantity="+this.state.quantity
        $.ajax({
            url: buyUrl,
            dataType: 'json',
            cache: false,
            success: function (data) {
                if(data["result"]){
                    alert("Transaction finished correctly!")
                    this.props.reloadProducts()
                    this.props.refreshTransactions()
                }else{
                    alert("There was an error. Please try again later.")
                }
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });

    },
    handleReserve: function(event) {
        var prods = this.props.products
        for (var i = 0; i < prods.length; i++){
            var obj = prods[i];
            if(obj["id"] == this.state.prod_selected && obj["currentQuantity"] < this.state.quantity){
                alert("NOT ENOUGH ITEMS OF THIS PRODUCT.")
                return;
            }
        }

        var reserveUrl = "/stock/reserveProduct?username="+this.props.username+"&prod_Id="+this.state.prod_selected+"&quantity="+this.state.quantity
        $.ajax({
            url: reserveUrl,
            dataType: 'json',
            cache: false,
            success: function (data) {
                if(data["result"]){
                    alert("Transaction finished correctly!")
                    this.props.reloadProducts()
                    this.props.refreshTransactions()
                }else{
                    alert("There was an error. Please try again later.")
                }
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    render: function() {
        if (this.props.products) {
            var productOptions = this.props.products.map(function(result,index) {
                return <OptionItem key={index} product={ result }/>
            });
        }
        return (
            <div className="panel panel-default">
                <h3>BUY/RESERVE PRODUCT PANEL</h3>
                <form>
                    <label>
                        Customer name: {this.props.username}
                    </label>
                    <select className="form-control" value={this.state.prod_selected} onChange={this.handleProductSelection}>
                        {productOptions}
                    </select>
                    <label>
                        Quantity: <input type="text" onChange={this.handleChangeQuantity}/>
                    </label>
                    <button type="button" onClick={this.handleBuy}>BUY</button>
                    <button type="button" onClick={this.handleReserve}>RESERVE</button>
                </form>
            </div>
            );
    }
});

var OptionItem = React.createClass({
    render: function() {
        return <option value={this.props.product.id}>{this.props.product.productName}</option>
    }
});

var Transactions_table = React.createClass({
    render:function(){
        if(this.props.transactions != null){
            var result = this.props.transactions.map(function(result,index){
                return <TransactionItem key={index} transaction={ result } reloadTransactionsTable={this.props.reloadTransactionsTable}/>
            }, this);
            return (
                <div className="panel panel-default">
                    <h3>CUSTOMER TRANSACTIONS PANEL</h3>
                        <div className="row">
                            <div className="col-md-12">
                                <table className="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>Transaction id</th>
                                        <th>Username</th>
                                        <th>Product name</th>
                                        <th>Transaction type</th>
                                        <th>Quantity</th>
                                        <th>Completed?</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        {result}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                </div>
            );
        }
        return (
            <div>
                LOADING!!!
            </div>
        );
    }
});

var TransactionItem = React.createClass({
    handleBuyReserved: function(e) {
            e.preventDefault();
            var finishReserveUrl = "/stock/finishReserve?transaction_id="+this.props.transaction.id
            $.ajax({
                url: finishReserveUrl,
                dataType: 'json',
                cache: false,
                success: function (rows) {
                    console.log("RESERVED COMPLETED CORRECTLY.")
                    this.props.reloadTransactionsTable()
                }.bind(this),
                error: function (xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }.bind(this)
            });
    },
    render:function(){
        return(
            <tr >
                <td>{this.props.transaction.id}</td>
                <td>{this.props.transaction.customer_name}</td>
                <td>{this.props.transaction.product_name}</td>
                <td>{this.props.transaction.trans_type}</td>
                <td>{this.props.transaction.quantity}</td>
                <td>{(this.props.transaction.finished==1 ? "YES "+(this.props.transaction.trans_type == "RESERVE" ? "(AFTER RESERVATION)" : "") : <button type="button" onClick={this.handleBuyReserved}>BUY RESERVATION</button>)}</td>
            </tr>
        );
    }
});

React.render(<MainContainer />, document.getElementById('maincontainer'));