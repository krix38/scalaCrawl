package pl.krix.scalacrawl

import java.net.URI
import org.jsoup.Jsoup
import scala.collection.JavaConversions._


object Crawler {

  def getDomain(URL: String): Option[String] = {               // get domain from URL method
    new URI(URL).getHost match {                               // get URI's host
      case s: String => Some(s.stripPrefix("www."))            // if got string, strip useless prefix
      case null => None                                        // if got null, return none
    }
  }

  def crawl(URL: String, visited: Set[String], interval: Int) {   // crawling method
    Thread.sleep(interval)                                        // sleep before launching a request
    Jsoup.connect(URL)                                            // connect
      .get()                                                      // get content
      .select("a[href]")                                          // get href elements from content (links)
      .map(_.attr("abs:href"))                                    // get their absolute path
      .filter(!_.isEmpty())                                       // weed out empty ones
      .filter(getDomain(_) == getDomain(URL))                     // we want links from same domain only
      .filter(!visited.contains(_))                               // we want unvisited links
      .foreach {                                                  // for every such link
        link:String => {                                          // execute lambda which
          println(URL + " --> " + link)                           // prints URL and its link
          crawl(link, visited + URL, interval)                    // crawl inside link
        }
      }
  }

  def printHelp() = {
    println("USAGE: sbt \"run [URL] [TIME INTERVAL BETWEEN REQUESTS]\"")
  }

  def main(args: Array[String]) {                              // run with arguments [URL] [TIME INTERVAL BETWEEN REQUESTS IN SECS]
    if(args.length < 2){
      printHelp()
    }else{
      crawl(args(0), Set[String](args(0)), args(1).toInt * 1000)
    }
  }
}
