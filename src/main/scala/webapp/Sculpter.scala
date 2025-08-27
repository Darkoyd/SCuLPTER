package webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

import webapp.components.Layout

@main
def Sculpter(): Unit =
    renderOnDomContentLoaded(dom.document.getElementById("app"), Layout.mainLayout(Router.render()))