import React from 'react'
import fetch from 'isomorphic-fetch'

const uri = 'http://localhost:8080'

export default class Report extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <section>
        <h2>Report</h2>
        <Top10 title="Top 10 countries with most airports" order="desc"/>
        <Top10 title="Top 10 countries with fewest airports" order="asc"/>
        <Surfaces/>
        <RunwayIdents/>
      </section>
    )
  }
}

class Top10 extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      countries: []
    }
  }

  componentDidMount() {
    fetch(uri + `/api/v1/reporting/countries/top?order=${this.props.order}`)
      .then(res => {
        if (res.status === 200) {
          return res.json()
        }
      })
      .then(countries => {this.setState({ countries })})
  }

  render() {
    let countries = (<span>Loading...</span>);
    if (this.state.countries.length) {
      countries = (
        <table>
          <tr><th>Code</th><th>Name</th><th>Airports</th></tr>
          {this.state.countries.map((c, i) => {return (<tr key={i}><td>{c.code}</td><td>{c.name}</td><td>{c.count}</td></tr>)})}
        </table>
      )
    }
    return (
      <section>
        <h3>{this.props.title}</h3>
        {countries}
      </section>
    )
  }
}

class Surfaces extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      surfaceFilter: null,
      countryFilter: null,
      surfaces: []
    }
    this.surfaceFilterChange = this.surfaceFilterChange.bind(this)
    this.countryFilterChange = this.countryFilterChange.bind(this)
  }

  componentDidMount() {
    fetch(uri + '/api/v1/reporting/countries/surfaces')
      .then(res => {
        if (res.status === 200) {
          return res.json()
        }
      })
      .then(surfaces => {this.setState({
        surfaces: surfaces,
        surfaceFilter: this.state.surfaceFilter,
        countryFilter: this.state.countryFilter
      })})
  }

  surfaceFilterChange(e) {
    this.setState({
      surfaces: this.state.surfaces,
      surfaceFilter: e.target.value,
      countryFilter: this.state.countryFilter
    })
  }

  countryFilterChange(e) {
    console.log('country filter ' + e.target.value)
    this.setState({
      surfaces: this.state.surfaces,
      surfaceFilter: this.state.surfaceFilter,
      countryFilter: e.target.value
    })
  }

  render() {
    let surfaces = (<span>Loading...</span>);
    if (this.state.surfaces.length) {
      surfaces = (
        <table>
          <tr><th>Country</th><th>Surface type</th></tr>
          <tr>
            <td><input type="text" value={this.state.countryFilter ? this.state.countryFilter : ''} onChange={this.countryFilterChange}/></td>
            <td><input type="text" value={this.state.surfaceFilter ? this.state.surfaceFilter : ''} onChange={this.surfaceFilterChange}/></td></tr>
          {this.state.surfaces
            .filter((s) => {
              let valid = true
              if (this.state.surfaceFilter !== null) {
                valid = s.surface && s.surface.startsWith(this.state.surfaceFilter)
              }
              if (valid && this.state.countryFilter !== null) {
                valid = s.name && s.name.startsWith(this.state.countryFilter)
              }
              return valid
            })
            .map((s, i) => {return (<tr key={i}><td>{s.name}</td><td>{s.surface}</td></tr>)})}
        </table>
      )
    }
    return (
      <section>
        <h3>Runway types per country</h3>
        {surfaces}
      </section>
    )
  }
}

class RunwayIdents extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      idents: []
    }
  }

  componentDidMount() {
    fetch(uri + '/api/v1/reporting/surfaces/idents')
      .then(res => {
        if (res.status === 200) {
          return res.json()
        }
      })
      .then(idents => {this.setState({ idents })})
  }

  render() {
    let idents = (<span>Loading...</span>);
    if (this.state.idents.length) {
      idents = (
        <table>
          <tr><th>Code</th><th>Ident. code</th><th>Runway count</th></tr>
          {this.state.idents.map((c, i) => {return (<tr key={i}><td>{c.ident}</td><td>{c.count}</td></tr>)})}
        </table>
      )
    }
    return (
      <section>
        <h3>Top 10 runway idents</h3>
        {idents}
      </section>
    )
  }
}
