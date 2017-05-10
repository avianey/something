//noinspection JSUnresolvedVariable
import styles from './assets/styles/style.scss'

import React from 'react'
import {render} from 'react-dom'

import App from './App'

export default (function () {
  try {
    renderApplication()
  } catch (error) {
    renderError(error)
  }
})()

function renderApplication () {
  render(
    <App />,
    document.getElementById('app')
  )
}

function renderError (error) {
  render(
    <div>
      <div>{error.toString()}</div>
    </div>,
    document.getElementById('app')
  )
}
