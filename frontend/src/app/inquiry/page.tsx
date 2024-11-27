'use client';

import React, { useState, useEffect } from "react";
import InquiryList from "@/components/inquiry/InquiryList";

export default function InquiryPage() {
  const [inquiries, setInquiries] = useState([]);

  useEffect(() => {
    const fetchInquiries = async () => {
      const res = await fetch("/api/inquiries"); // 백엔드 API 호출
      const data = await res.json();
      setInquiries(data.content); // 받아온 데이터 저장
    };

    fetchInquiries();
  }, []);

  return (
      <div>
        <h1 className="text-2xl font-bold mb-6">문의 리스트</h1>
        <InquiryList inquiries={inquiries} onInquirySelect={(id) => console.log(id)} />
      </div>
  );
}
