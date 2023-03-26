import Head from 'next/head'
import {Inter} from 'next/font/google'
import React from "react";

const inter = Inter({subsets: ['latin']})

export default function Home() {
  return (
    <>
      <Head>
        <title>Bibliothecarius</title>
        <meta name="description" content="Bibliothecarius, generate your own library powered by AI." />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <div className="container mx-auto p-4">
        <h1 className="text-2xl mb-4">Homepage</h1>
        <h2>Welcome to Bibliothecarius</h2>
      </div>
    </>
  )
}
