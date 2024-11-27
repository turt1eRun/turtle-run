'use client';

import "../../styles/globals.css";
import Sidebar from "@/components/common/Sidebar";
import React from "react";
import Header from "@/components/common/Header";
import { useSidebarStore } from "@/store/sidebarStore";
import SidebarSimple from "@/components/common/SidebarSimple";

export default function InquiryLayout({
                                        children,
                                      }: {
  children: React.ReactNode;
}) {
  const { isExpanded } = useSidebarStore();

  return (
      <>
        {/* 공통 헤더 */}
        <header>
          <Header />
        </header>

        {/* 사이드바 + 메인 콘텐츠 */}
        <main>
          <div className="flex">
            {/* 사이드바 (확장/축소 상태) */}
            {isExpanded ? <Sidebar /> : <SidebarSimple />}

            {/* 하위 페이지 콘텐츠 */}
            <div className="flex-1 p-8 bg-gray-100">{children}</div>
          </div>
        </main>

        {/* 공통 푸터 */}
        <footer className="p-4 bg-gray-800 text-white text-center">
          © 2024 Your Company. All rights reserved.
        </footer>
      </>
  );
}
