'use client'
import "../../styles/globals.css";
import Sidebar from "@/components/common/Sidebar";
import React from "react";
import Header from "@/components/common/Header";
import {useSidebarStore} from "@/store/sidebarStore";
import SidebarSimple from "@/components/common/SidebarSimple";

export default function MainLayout({
                                     children,
                                   }: Readonly<{
  children: React.ReactNode;
}>) {
  const { isExpanded } = useSidebarStore()
  return (
      <>
        <header>
          <Header/>
        </header>
        <main>
          <div className="flex">
            {
              isExpanded
                ? <Sidebar/>
                  : <SidebarSimple/>
            }

            <div className="flex-1">
              {children}
            </div>
          </div>
        </main>
        <footer>

        </footer>
      </>

  )
      ;
}
