var MainContainer = React.createClass({
    getInitialState: function () {
        return {
            data: null
        };
    },
    componentDidMount: function () {
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
                    <ResultTable result={this.state.data}/>
                    <CustomerPanel result={this.state.data}/>
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

var ResultTable = React.createClass({
    render:function(){
        if(this.props.result){
            var result = this.props.result.map(function(result,index){
                return <ResultItem key={index} product={ result }/>
            });
            return(
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
    refill: function (e) {
        //e.preventDefault();

        var refillUrl = "/stock/refillProduct?prod_id="+this.props.product.id+"&quantity="+this.state.refill_number
        $.ajax({
            url: refillUrl,
            dataType: 'json',
            cache: false,
            success: function (rows) {
                console.log("PRODUCT REFILLED CORRECTLY.")
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
    getInitialState: function () {
        return {
            prod_selected: ""
        };
    },
    handleProductSelection: function(event) {
        this.setState({prod_selected: event.target.value});
    },
    handleChangeQuantity: function(event) {
        this.setState({quantity: event.target.value});
    },
    handleChangeName: function(event) {
        this.setState({customer_name: event.target.value});
    },
    handleBuy: function(event) {
        alert("BUYING - "+this.state.customer_name + " - "+ this.state.quantity + " - "+ this.state.prod_selected)
    },
    handleReserve: function(event) {
        alert("RESERVING - "+this.state.customer_name + " - "+ this.state.quantity + " - "+ this.state.prod_selected)
    },
    render:function(){
        if(this.props.result){
            var productOptions = this.props.result.map(function(result,index){
                return <OptionItem key={index} product={ result }/>
        });
        return(
                <div>
                    <h1>CUSTOMER PANEL</h1>
                    <form>
                        <label>
                            Customer name: <input type="text" onChange={this.handleChangeName}/>
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
        return (
            <div>
                LOADING!!!
            </div>
        );
    }
});

var OptionItem = React.createClass({
    render: function() {
        return <option value={this.props.product.id}>{this.props.product.productName}</option>
    }
});

React.render(<MainContainer />, document.getElementById('maincontainer'));