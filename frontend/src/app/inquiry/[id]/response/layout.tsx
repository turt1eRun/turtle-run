'use client';

import React from "react";

export default function ResponseLayout({
                                         children,
                                       }: {
  children: React.ReactNode;
}) {
  return (
      <div>
        <header className="p-4 bg-gray-200">
          <h1 className="text-xl font-bold">문의</h1>
        </header>
        <main className="p-8">{children}</main>
      </div>
  );
}
