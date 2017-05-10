import React from 'react'
import fetch from 'isomorphic-fetch'

const uri = 'http://localhost:8080'

export default class Search extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      q: '',
      runways: []
    }
    this.queryChange = this.queryChange.bind(this)
    this.searchClicked = this.searchClicked.bind(this)
  }

  queryChange(e) {
    this.setState({ q: e.target.value });
  }

  searchClicked(e) {
    fetch(uri + `/api/v1/search?q=${this.state.q}`)
      .then(res => {
        if (res.status === 200) {
          return res.json()
        }
      }).then(runways => {
        this.setState({ q: this.state.q, runways: runways })
      })
    e.preventDefault()
  }

  render() {
    let runways;
    if (this.state.runways) {
      runways = (
        <table>
          <tr><th>Airport name</th><th>Airport code</th><th>Country</th><th>Runway ident</th><th>Runway surface</th></tr>
          {this.state.runways.map((r, i) => {
            return (
              <tr key={i}>
                <td>{r.airport.name}</td>
                <td>{r.airport.code}</td>
                <td>{r.airport.country.name}</td>
                <td>{r.ident}</td>
                <td>{r.surface}</td>
              </tr>)
          })}
        </table>
      )
    }
    return (
      <section>
        <h2>Search</h2>
        <form>
          <label htmlFor="input">Enter search query</label>&nbsp;
          <input type="text" classID="input" name="input" onChange={this.queryChange} value={this.state.q}/>&nbsp;
          <input type="button" onClick={this.searchClicked} value="Search"/>
        </form>
        <hr/>
        {runways}
      </section>
    )
  }
}

